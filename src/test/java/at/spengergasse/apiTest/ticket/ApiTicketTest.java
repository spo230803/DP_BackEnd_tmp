package at.spengergasse.apiTest.ticket;
import at.spengergasse.ApiBaseTest;
import at.spengergasse.MyFactory;
import at.spengergasse.dp_backend.dto.ticket.TicketsRequest;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.models.ticket.Discounts;
import at.spengergasse.dp_backend.models.ticket.TicketKategorie;
import at.spengergasse.dp_backend.models.ticket.Tickets;
import at.spengergasse.dp_backend.models.users.Users;
import at.spengergasse.dp_backend.service.event.EventsService;
import at.spengergasse.dp_backend.service.ticket.DiscountsService;
import at.spengergasse.dp_backend.service.ticket.TicketKategorieService;
import at.spengergasse.dp_backend.service.ticket.TicketService;
import at.spengergasse.dp_backend.service.users.UsersService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
public class ApiTicketTest extends ApiBaseTest
{
    private static final String API_BASE = "/api/Tickets";

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private EventsService eventsService;

    @Autowired
    private TicketKategorieService ticketKategorieService;

    @Autowired
    private DiscountsService discountsService;

    private TicketHelp createKey()
    {
        Users user = usersService.save(MyFactory.user());
        Events event = eventsService.save(MyFactory.event());
        TicketKategorie ticketKategorie = ticketKategorieService.save(MyFactory.ticketKategorie());
        Discounts discount = discountsService.save(MyFactory.discount());

        ExistingElementKey2 elementKey = findExistingElementKey();

        em.flush();
        em.clear();

        return new TicketHelp(user, event, ticketKategorie, discount, elementKey);
    }

    private ExistingElementKey2 findExistingElementKey()
    {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createNativeQuery("""
                SELECT seat_plan_id, x, y
                FROM elements
                LIMIT 1
                """).getResultList();

        Assertions.assertFalse(
                rows.isEmpty(),
                "La tabella elements è vuota. Serve almeno un record in elements per i test di ApiTicket."
        );

        Object[] row = rows.getFirst();

        Long seatPlanId = ((Number) row[0]).longValue();
        Integer x = ((Number) row[1]).intValue();
        Integer y = ((Number) row[2]).intValue();

        return new ExistingElementKey2(seatPlanId, x, y);
    }

    private TicketsRequest createRequest(TicketHelp help)
    {
        return new TicketsRequest(
                "OFFEN",
                help.getUser().getId(),
                help.getEvent().getId(),
                null,
                help.getTicketKategorie().getId(),
                new BigDecimal("33.50"),
                help.getElementKey().getSeatPlanId(),
                help.getElementKey().getX(),
                help.getElementKey().getY()
        );
    }

    private TicketsRequest createRequestWithDiscount(TicketHelp help)
    {
        return new TicketsRequest(
                "OFFEN",
                help.getUser().getId(),
                help.getEvent().getId(),
                help.getDiscount().getId(),
                help.getTicketKategorie().getId(),
                new BigDecimal("33.50"),
                help.getElementKey().getSeatPlanId(),
                help.getElementKey().getX(),
                help.getElementKey().getY()
        );
    }

    private Tickets createTicket(TicketHelp help)
    {
        Tickets ticket = ticketService.save(createRequest(help));
        em.flush();
        em.clear();
        return ticket;
    }

    @Test
    void findAllOkTest() throws Exception
    {
        TicketHelp help = createKey();
        createTicket(help);

        mockMvc.perform(get(API_BASE + "/getAll").session(session))
                .andExpect(status().isOk());
    }



    @Test
    void findByIdOkTest() throws Exception
    {
        TicketHelp help = createKey();
        Tickets ticket = createTicket(help);

        mockMvc.perform(get(API_BASE + "/get/" + ticket.getId()).session(session))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdErrorTest() throws Exception
    {
        mockMvc.perform(get(API_BASE + "/get/-1").session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOkTest() throws Exception
    {
        TicketHelp help = createKey();
        TicketsRequest request = createRequest(help);

        mockMvc.perform(post(API_BASE)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createOkWithDiscountTest() throws Exception
    {
        TicketHelp help = createKey();
        TicketsRequest request = createRequestWithDiscount(help);

        mockMvc.perform(post(API_BASE)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createErrorBadRequestTest() throws Exception
    {
        TicketHelp help = createKey();

        String json = """
                {
                  "status": "",
                  "user_id": %d,
                  "event_id": %d,
                  "discount_id": null,
                  "ticket_kategorie_id": %d,
                  "price": 33.50,
                  "elements_seat_plan_id": %d,
                  "elements_x": %d,
                  "elements_y": %d
                }
                """.formatted(
                help.getUser().getId(),
                help.getEvent().getId(),
                help.getTicketKategorie().getId(),
                help.getElementKey().getSeatPlanId(),
                help.getElementKey().getX(),
                help.getElementKey().getY()
        );

        mockMvc.perform(post(API_BASE)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateOkTest() throws Exception
    {
        TicketHelp help = createKey();
        Tickets ticket = createTicket(help);

        TicketsRequest updateRequest = new TicketsRequest(
                "BEZAHLT",
                help.getUser().getId(),
                help.getEvent().getId(),
                null,
                help.getTicketKategorie().getId(),
                new BigDecimal("44.90"),
                help.getElementKey().getSeatPlanId(),
                help.getElementKey().getX(),
                help.getElementKey().getY()
        );

        mockMvc.perform(put(API_BASE + "/" + ticket.getId())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void updateErrorNotFoundTest() throws Exception
    {
        TicketHelp help = createKey();
        TicketsRequest updateRequest = createRequest(help);

        mockMvc.perform(put(API_BASE + "/-1")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOkTest() throws Exception
    {
        TicketHelp help = createKey();
        Tickets ticket = createTicket(help);

        mockMvc.perform(delete(API_BASE + "/" + ticket.getId()).session(session))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteErrorTest() throws Exception
    {
        mockMvc.perform(delete(API_BASE + "/-1").session(session))
                .andExpect(status().isNotFound());
    }
}


class TicketHelp
{
    private final Users user;
    private final Events event;
    private final TicketKategorie ticketKategorie;
    private final Discounts discount;
    private final ExistingElementKey2 elementKey;

    public TicketHelp(Users user, Events event, TicketKategorie ticketKategorie, Discounts discount, ExistingElementKey2 elementKey)
    {
        this.user = user;
        this.event = event;
        this.ticketKategorie = ticketKategorie;
        this.discount = discount;
        this.elementKey = elementKey;
    }

    public Users getUser()
    {
        return user;
    }

    public Events getEvent()
    {
        return event;
    }

    public TicketKategorie getTicketKategorie()
    {
        return ticketKategorie;
    }

    public Discounts getDiscount()
    {
        return discount;
    }

    public ExistingElementKey2 getElementKey()
    {
        return elementKey;
    }
}

class ExistingElementKey
{
    private final Long seatPlanId;
    private final Integer x;
    private final Integer y;

    public ExistingElementKey(Long seatPlanId, Integer x, Integer y)
    {
        this.seatPlanId = seatPlanId;
        this.x = x;
        this.y = y;
    }

    public Long getSeatPlanId()
    {
        return seatPlanId;
    }

    public Integer getX()
    {
        return x;
    }

    public Integer getY()
    {
        return y;
    }
}