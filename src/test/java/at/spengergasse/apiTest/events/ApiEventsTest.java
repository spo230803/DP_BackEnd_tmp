package at.spengergasse.apiTest.events;

//import at.spengergasse.dp_backend.MyFactory;
import at.spengergasse.ApiBaseTest;
import at.spengergasse.MyFactory;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.service.event.EventsService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc(addFilters = false)
class ApiEventsTest extends ApiBaseTest {

    private static final String API_BASE = "/api/Events";

    @Autowired
    private EventsService eventsService = new EventsService();


    @Test
    void findAllEventsTest() throws Exception {
        Events event = MyFactory.event();
        eventsService.save(event);

        em.flush();
        em.clear();

        mockMvc.perform(get(API_BASE).session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findEventByIdOkayTest() throws Exception {
        Events event = MyFactory.event();
        Events saved = eventsService.save(event);
        em.flush();
        Long id = saved.getId();   // id garantito dopo flush
        em.clear();

        mockMvc.perform(get(API_BASE + "/" + id).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }
    @Test
    void findEventByIdFailTest() throws Exception {
        mockMvc.perform(get(API_BASE + "/-1").session(session))
                .andExpect(status().isNotFound());
    }


    @Test
    void createEventTestJsonManuale() throws Exception {
        String json = """
            {
              "title": "Concerto di prova",
              "subtitle": "Evento test API",
              "startsDate": "2026-03-10T18:30",
              "endsDate": "2026-03-10T20:30",
              "location": "Vienna Hall",
              "iban": "AT611904300234573201",
              "status": "OFFEN"
            }
            """;

        mockMvc.perform(post(API_BASE)
                        .session(session)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Concerto di prova"));
    }

    @Test
    void createEventBadRequestWithoutTitleTest() throws Exception {
        String json = """
            {
              "title": "",
              "subtitle": "Evento test API",
              "startsDate": "2026-03-10T18:30",
              "endsDate": "2026-03-10T20:30",
              "location": "Vienna Hall",
              "iban": "AT611904300234573201",
              "status": "OFFEN"
            }
            """;

        mockMvc.perform(post(API_BASE)
                        .session(session)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteByIdTest() throws Exception {
        Events event = MyFactory.event();
        Events saved = eventsService.save(event);
        em.flush();
        Long id = saved.getId();
        em.clear();

        mockMvc.perform(delete(API_BASE + "/" + id).session(session))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteByIdFailTest() throws Exception {
        mockMvc.perform(delete(API_BASE + "/-1").session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateOkayTest() throws Exception
    {
        Events event = MyFactory.event();
        event = eventsService.save(event);

        String json = """
 {
              "title": "Concerto di prova",
              "subtitle": "Evento test API",
              "startsDate": "2026-03-10T18:30",
              "endsDate": "2026-03-10T20:30",
              "location": "Vienna Hall",
              "iban": "AT611904300234573201",
              "status": "OFFEN"
            }
            """;
        mockMvc.perform(put(API_BASE+"/"+event.getId())
                        .session(session)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());

    }



    @Test
    void updateErrorTest() throws Exception
    {
        Events event = MyFactory.event();
        event = eventsService.save(event);

        String json = """
        {
              "title": "",
              "subtitle": "Evento test API",
              "startsDate": "2026-03-10T18:30",
              "endsDate": "2026-03-10T20:30",
              "location": "Vienna Hall",
              "iban": "AT611904300234573201",
              "status": "OFFEN"
            }
            """;
        mockMvc.perform(put(API_BASE+"/"+event.getId())
                        .session(session)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

}