package at.spengergasse.apiTest.ticket;

import at.spengergasse.ApiBaseTest;
import at.spengergasse.MyFactory;
import at.spengergasse.dp_backend.dto.ticket.TicketKategorienEventsRequest;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.models.ticket.TicketKategorie;
import at.spengergasse.dp_backend.service.event.EventsService;
import at.spengergasse.dp_backend.service.ticket.TicketKategorieService;
import at.spengergasse.dp_backend.service.ticket.TicketKategorienEventsService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
public class ApiTicketKategorienEventsTest extends ApiBaseTest
{
    private static final String API_BASE = "/api/Events";

    @Autowired
    private TicketKategorienEventsService ticketKategorienEventsService;

    @Autowired
    private EventsService eventsService;

    @Autowired
    private TicketKategorieService ticketKategorieService;

    private Helps createKey()
    {
        Events event = MyFactory.event();
        TicketKategorie ticketKategorie = MyFactory.ticketKategorie();

        event = eventsService.save(event);
        ticketKategorie = ticketKategorieService.save(ticketKategorie);

        em.flush();
        em.clear();

        return new Helps(event, ticketKategorie);
    }

    @Test
    void findAllOkTest() throws Exception
    {
        Helps helps = createKey();

        mockMvc.perform(get(API_BASE + "/" + helps.getEvent().getId() + "/TicketKategorien")
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdOkTest() throws Exception
    {
        Helps helps = createKey();

        TicketKategorienEventsRequest request = new TicketKategorienEventsRequest(
                helps.getEvent().getId(),
                helps.getTicketKategorie().getId(),
                new BigDecimal("33.50")
        );

        ticketKategorienEventsService.addTicketKategorieToEvent(helps.getEvent().getId(), request);
        em.flush();
        em.clear();

        mockMvc.perform(get(API_BASE + "/" + helps.getEvent().getId() + "/TicketKategorien/" + helps.getTicketKategorie().getId())
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdErrorTest() throws Exception
    {
        mockMvc.perform(get(API_BASE + "/-1/TicketKategorien/-1")
                        .session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOkTest() throws Exception
    {
        Helps helps = createKey();

        TicketKategorienEventsRequest request = new TicketKategorienEventsRequest(
                helps.getEvent().getId(),
                helps.getTicketKategorie().getId(),
                new BigDecimal("33.50")
        );

        mockMvc.perform(post(API_BASE + "/" + helps.getEvent().getId() + "/TicketKategorien")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createErrorBadRequestTest() throws Exception
    {
        Helps helps = createKey();

        String json = """
                {
                  "eventId": %d,
                  "ticketKategorienId": %d,
                  "price": -1.00
                }
                """.formatted(helps.getEvent().getId(), helps.getTicketKategorie().getId());

        mockMvc.perform(post(API_BASE + "/" + helps.getEvent().getId() + "/TicketKategorien")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateOkTest() throws Exception
    {
        Helps helps = createKey();

        TicketKategorienEventsRequest createRequest = new TicketKategorienEventsRequest(
                helps.getEvent().getId(),
                helps.getTicketKategorie().getId(),
                new BigDecimal("33.50")
        );

        ticketKategorienEventsService.addTicketKategorieToEvent(helps.getEvent().getId(), createRequest);
        em.flush();
        em.clear();

        TicketKategorienEventsRequest updateRequest = new TicketKategorienEventsRequest(
                helps.getEvent().getId(),
                helps.getTicketKategorie().getId(),
                new BigDecimal("44.90")
        );

        mockMvc.perform(put(API_BASE + "/" + helps.getEvent().getId() + "/TicketKategorien/" + helps.getTicketKategorie().getId())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void updateErrorNotFoundTest() throws Exception
    {
        Helps helps = createKey();

        TicketKategorienEventsRequest updateRequest = new TicketKategorienEventsRequest(
                helps.getEvent().getId(),
                helps.getTicketKategorie().getId(),
                new BigDecimal("44.90")
        );

        mockMvc.perform(put(API_BASE + "/" + helps.getEvent().getId() + "/TicketKategorien/" + helps.getTicketKategorie().getId())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOkTest() throws Exception
    {
        Helps helps = createKey();

        TicketKategorienEventsRequest request = new TicketKategorienEventsRequest(
                helps.getEvent().getId(),
                helps.getTicketKategorie().getId(),
                new BigDecimal("33.50")
        );

        ticketKategorienEventsService.addTicketKategorieToEvent(helps.getEvent().getId(), request);
        em.flush();
        em.clear();

        mockMvc.perform(delete(API_BASE + "/" + helps.getEvent().getId() + "/TicketKategorien/" + helps.getTicketKategorie().getId())
                        .session(session))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteErrorTest() throws Exception
    {
        Helps helps = createKey();

        mockMvc.perform(delete(API_BASE + "/" + helps.getEvent().getId() + "/TicketKategorien/" + helps.getTicketKategorie().getId())
                        .session(session))
                .andExpect(status().isNotFound());
    }
}

class Helps
{
    private final Events event;
    private final TicketKategorie ticketKategorie;

    public Helps(Events event, TicketKategorie ticketKategorie)
    {
        this.event = event;
        this.ticketKategorie = ticketKategorie;
    }

    public Events getEvent()
    {
        return event;
    }

    public TicketKategorie getTicketKategorie()
    {
        return ticketKategorie;
    }
}