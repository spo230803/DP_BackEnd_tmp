package at.spengergasse.apiTest.users;

import at.spengergasse.ApiBaseTest;
import at.spengergasse.ApiBaseTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
public class ApiParticipantsTest extends ApiBaseTest
{
    private static final String API_BASE = "/api/Participants";

    private Long findExistingEventId()
    {
        @SuppressWarnings("unchecked")
        List<Number> rows = em.createNativeQuery("""
                SELECT id
                FROM events
                ORDER BY id
                LIMIT 1
                """).getResultList();

        assertFalse(rows.isEmpty(), "La tabella events è vuota, serve almeno un evento per i test.");

        return rows.getFirst().longValue();
    }

    @Test
    void getAllByEventOkTest() throws Exception
    {
        Long eventId = findExistingEventId();

        mockMvc.perform(get(API_BASE + "/getAllByEvent/" + eventId)
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    void getAllByEventErrorTest() throws Exception
    {
        mockMvc.perform(get(API_BASE + "/getAllByEvent/-1")
                        .session(session))
                .andExpect(status().isNotFound());
    }
}
