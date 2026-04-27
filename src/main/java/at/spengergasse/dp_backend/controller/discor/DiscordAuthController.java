package at.spengergasse.dp_backend.controller.discor;


import at.spengergasse.dp_backend.controller.BaseController;
import at.spengergasse.dp_backend.dto.discor.DiscordUserRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.service.discor.DiscordService;
import at.spengergasse.dp_backend.service.users.UsersService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth/discord")
public class DiscordAuthController extends BaseController
{


    private final DiscordService discordService;

	@Value("${frontend.base-url}")
    private String frontendBaseUrl;
    
    @Value("${discord.client-id}")
    private String clientId;

    @Value("${discord.member-role-id}")
    private String memberRoleId;

    @Value("${discord.admin-role-id}")
    private String adminRoleId;

    @Value("${discord.redirect-uri}")
    private String redirectUri;

    @Autowired
    private UsersService usersService;

    public DiscordAuthController(DiscordService discordService) {
        this.discordService = discordService;
    }


    /**
     * Funzone da chiamare dopo aver fatto il LogIn su Discovery
     * @param code
     * @param state
     * @param request
     * @return
     */
    @GetMapping("/callback")
    public ResponseEntity<Object> callback(
            @RequestParam String code,
            @RequestParam String state,
            HttpServletRequest request
    )
    {
        String expectedState = (String) session.getAttribute("OAUTH_STATE");
        if (expectedState == null || !expectedState.equals(state)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid state");
        }

        String accessToken = discordService.exchangeCodeForToken(code); //Usato per i Dati
        DiscordUserRequest me = discordService.fetchMe(accessToken); //Estraggo i Dati Utenti

        //Savlo in Sessione
        session.setAttribute(SESSION_DISCORD_AUTH_KEY, true);         //User LogIn OKAY
        session.setAttribute(SESSION_DISCROD_USER, me);                     // Info von Discord
        session.setAttribute(SESSION_DISCROD_ACCESS_TOKEN, accessToken);    //Access Discrd Token
        session.setAttribute(SESSION_USER_IP, request.getRemoteAddr());     // Salvo IP remoto del Client

        String discordUserId = me.id().toString();


        var member = discordService.fetchGuildMember(discordUserId);

        boolean isMember = (member != null && member.getRoles() != null)
                && member.getRoles().contains(memberRoleId);

        boolean isAdmin = isMember
                && member.getRoles().contains(adminRoleId);

        // Session speichern
        session.setAttribute(SESSION_IS_MEMBER, isMember);
        session.setAttribute(SESSION_IS_ADMIN, isAdmin);

        var authorities = new java.util.ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (isMember) authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
        if (isAdmin)  authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        //autenticazione per Spring Security
        var auth = new UsernamePasswordAuthenticationToken(
                discordUserId,                      // principal (anche me o me.getUsername())
                null,
                authorities
        );


        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // salva il SecurityContext in sessione (così resta anche nelle richieste successive)
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );

        //Spechen der User Login
        Long idUserUsereDB = usersService.userLogIn(me);
        session.setAttribute(SESSION_ID_USER_UNSERE_DB, idUserUsereDB);

        //Wenn die LogIn Okay ist -> Redirect to FrontEnd
        //return ResponseEntity.status(HttpStatus.OK).location(URI.create("/booking")).build();
        String target;
        if (!isMember) {
            target = frontendBaseUrl + "/admin";
            //return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    // Wird noch eine 'kein Zugriff' seite erstellt
              //      .body("Kein Zugriff: Du bist kein Vereinsmitglied");
        } else if (isAdmin) {
            target = frontendBaseUrl + "/admin";
        } else {
            target = frontendBaseUrl + "/events";
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(target))
                .build();

    }//end Function

    /**
     * Facci il logOut - Distruggo la Sessione
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }


    /**
     * Funzione che fa il Redirecht alle AIP di Discord per LOGIN
     * @param response
     * @param session
     * @throws IOException
     */
    @GetMapping("/login")
    public void login(HttpServletResponse response, HttpSession session) throws IOException {
        String state = java.util.UUID.randomUUID().toString();
        session.setAttribute("OAUTH_STATE", state);

        String authorizeUrl =
                "https://discord.com/api/oauth2/authorize" +
                        "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                        "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                        "&response_type=code" +
                        "&scope=" + URLEncoder.encode("identify email", StandardCharsets.UTF_8) +
                        "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8);

        response.sendRedirect(authorizeUrl);
    }


    /**
     * Funzone per sapere lo stato del LogIn
     * @param session
     * @return Info Login
     */
    @GetMapping(value = "/status", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getStatus(HttpSession session, HttpServletRequest request)
    {

        if (!Boolean.TRUE.equals(session.getAttribute(SESSION_DISCORD_AUTH_KEY))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        DiscordUserRequest user = (DiscordUserRequest) session.getAttribute(SESSION_DISCROD_USER);

        if(user == null) {
            throw new ExeException("User is null", this );
        }

        HashMap<String, String> infoLogin = new HashMap<>();
        infoLogin.put("UserId", user.id().toString());
        infoLogin.put("UserName", user.username());
        infoLogin.put("UserEmail", user.email());
        infoLogin.put("UserAvatar", user.avatar());
        infoLogin.put("IdSession", session.getId());
        infoLogin.put("RemoteIP", request.getRemoteAddr());
        infoLogin.put("RemoteHost", request.getRemoteHost());
        infoLogin.put("UserAgent", request.getHeader("User-Agent"));
        infoLogin.put("IsAdmin",  session.getAttribute(SESSION_IS_ADMIN).toString());

        Gson gson = new Gson();
        String json = gson.toJson(infoLogin);

        return ResponseEntity.ok(json);

    }

    @GetMapping("/getUser")
    public ResponseEntity<Object> getUser(HttpSession session)
    {
        if (!Boolean.TRUE.equals(session.getAttribute(SESSION_DISCORD_AUTH_KEY))) {
            throw ExeException.notLoggin();
        }
        DiscordUserRequest discord = (DiscordUserRequest) session.getAttribute(SESSION_DISCROD_USER);
        Long dbId = (Long) session.getAttribute(SESSION_ID_USER_UNSERE_DB);

        var result = new java.util.HashMap<String, Object>();
        result.put("id", discord.id());
        result.put("dbId", dbId);
        result.put("username", discord.username());
        result.put("email", discord.email());
        result.put("avatar", discord.avatar());

        return ResponseEntity.ok(result);
    }

/***********************************************************************************/
    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}//end  Class
