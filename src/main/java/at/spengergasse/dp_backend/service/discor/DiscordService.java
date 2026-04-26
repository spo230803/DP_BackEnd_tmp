package at.spengergasse.dp_backend.service.discor;

import at.spengergasse.dp_backend.dto.discor.DiscordGuildMember;
import at.spengergasse.dp_backend.dto.discor.DiscordUserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class DiscordService {

    private final RestTemplate restTemplate = new RestTemplate();

	@Value("${discord.bot-token}")
    private String botToken;

    @Value("${discord.guild-id}")
    private String guildId;

    @Value("${discord.admin-role-id}")
    private String adminRoleId;

    @Value("${discord.member-role-id}")
    private String memberRoleId;

    @Value("${discord.client-id}")
    private String clientId;

    @Value("${discord.client-secret}")
    private String clientSecret;

    @Value("${discord.redirect-uri}")
    private String redirectUri;


    public String exchangeCodeForToken(String code) {
        String tokenUrl = "https://discord.com/api/oauth2/token";

        System.out.println("Code Discord  : " + code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(form, headers);

        try {
            ResponseEntity<Map> res = restTemplate.exchange(tokenUrl, HttpMethod.POST, req, Map.class);
            Map body = res.getBody();
            if (body == null || body.get("access_token") == null) {
                throw new RuntimeException("Token response missing access_token: " + body);
            }
            return body.get("access_token").toString();
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            // 🔍 così vedi eventuale body (se c'è)
            throw new RuntimeException("Discord token error: " + ex.getStatusCode() + " body=" + ex.getResponseBodyAsString(), ex);
        }
    }

    /**
     * Funzone per Estrare i Dati Utente dalle API di Discord
     * @param accessToken
     * @return
     */
    public DiscordUserRequest fetchMe(String accessToken) {
        String meUrl = "https://discord.com/api/users/@me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> req = new HttpEntity<>(headers);

        ResponseEntity<DiscordUserRequest> res = restTemplate.exchange(meUrl, HttpMethod.GET, req, DiscordUserRequest.class);

        DiscordUserRequest user = res.getBody();
        if (user == null) throw new RuntimeException("Discord /users/@me returned empty body");

        return user;
    }


    /** Holt die Guild-Member-Info (inkl. roles[]) via Bot Token */
    public DiscordGuildMember fetchGuildMember(String userId) {
        String url = "https://discord.com/api/guilds/" + guildId + "/members/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bot " + botToken);

        HttpEntity<Void> req = new HttpEntity<>(headers);

        try {
            ResponseEntity<DiscordGuildMember> res =
                    restTemplate.exchange(url, HttpMethod.GET, req, DiscordGuildMember.class);

            DiscordGuildMember member = res.getBody();
            if (member == null) return null;
            return member;

        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            // User ist nicht in der Guild
            return null;
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            throw new RuntimeException("Discord guild member error: " + ex.getStatusCode()
                    + " body=" + ex.getResponseBodyAsString(), ex);
        }
    }

    /** Vereinsmitglied? (hat memberRoleId) */
    public boolean isVereinsmitglied(String userId) {
        DiscordGuildMember member = fetchGuildMember(userId);
        if (member == null || member.getRoles() == null) return false;
        return member.getRoles().contains(memberRoleId);
    }

    /** Admin? (hat adminRoleId) */
    public boolean isAdminInGuild(String userId) {
        DiscordGuildMember member = fetchGuildMember(userId);
        if (member == null || member.getRoles() == null) return false;
        return member.getRoles().contains(adminRoleId);
    }

}//end class
