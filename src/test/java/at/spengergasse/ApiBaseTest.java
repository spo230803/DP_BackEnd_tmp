package at.spengergasse;

import at.spengergasse.dp_backend.DpBackEndApplication;
import at.spengergasse.dp_backend.dto.discor.DiscordUserRequest;
import at.spengergasse.dp_backend.mapper.UserMapper;
import at.spengergasse.dp_backend.models.users.Users;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest(classes = DpBackEndApplication.class)
@ActiveProfiles("dev")
public class ApiBaseTest
{
    protected MockHttpSession session;

    @Autowired
    protected EntityManager em;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * Simulo il login di un Utente
     */
    @BeforeEach
    void simulaLogIn() {
        session = new MockHttpSession();
        session.setAttribute("DISCORD_AUTHENTICATED", true);

        DiscordUserRequest user = UserMapper.toRequest(
            new Users(
                1L,
                "UserName Test",
                "test@dp.at"
            )
        );

        session.setAttribute("DISCORD_USER", user);
        session.setAttribute("SESSION_ID_USER_UNSERE_DB", 1);
    }
}
