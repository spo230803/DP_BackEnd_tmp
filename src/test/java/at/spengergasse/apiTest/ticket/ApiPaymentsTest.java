package at.spengergasse.apiTest.ticket;

import at.spengergasse.ApiBaseTest;
import at.spengergasse.MyFactory;
import at.spengergasse.dp_backend.dto.ticket.PaymentsRequest;
import at.spengergasse.dp_backend.dto.ticket.TicketsRequest;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.models.ticket.Payments;
import at.spengergasse.dp_backend.models.ticket.TicketKategorie;
import at.spengergasse.dp_backend.models.ticket.Tickets;
import at.spengergasse.dp_backend.models.users.Users;
import at.spengergasse.dp_backend.service.event.EventsService;
import at.spengergasse.dp_backend.service.ticket.TicketKategorieService;
import at.spengergasse.dp_backend.service.ticket.TicketService;
import at.spengergasse.dp_backend.service.ticket.PaymentsService;
import at.spengergasse.dp_backend.service.users.UsersService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
public class ApiPaymentsTest extends ApiBaseTest
{

    private static final String API_BASE = "/api/Payments";

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private EventsService eventsService;

    @Autowired
    private TicketKategorieService ticketKategorieService;


    private PaymentHelp createKey()
    {
        Users user = usersService.save(MyFactory.user());
        Events event = eventsService.save(MyFactory.event());
        TicketKategorie ticketKategorie = ticketKategorieService.save(MyFactory.ticketKategorie());

        ExistingElementKey2 elementKey = findExistingElementKey();

        em.flush();
        em.clear();

        return new PaymentHelp(user, event, ticketKategorie, elementKey);
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
                "La tabella elements è vuota. Serve almeno un record in elements per i test di ApiPayments."
        );

        Object[] row = rows.getFirst();

        Long seatPlanId = ((Number) row[0]).longValue();
        Integer x = ((Number) row[1]).intValue();
        Integer y = ((Number) row[2]).intValue();

        return new ExistingElementKey2(seatPlanId, x, y);
    }

    private TicketsRequest createTicketRequest(PaymentHelp help)
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

    private Tickets createTicket(PaymentHelp help)
    {
        Tickets ticket = ticketService.save(createTicketRequest(help));
        em.flush();
        em.clear();
        return ticket;
    }

    private PaymentsRequest createPaymentRequest(Tickets ticket)
    {
        return new PaymentsRequest(
                new BigDecimal("33.50"),
                "CARD",
                "PAID",
                LocalDateTime.of(2026, 3, 13, 10, 30),
                ticket.getId()
        );
    }

    private Payments createPayment(PaymentHelp help)
    {
        Tickets ticket = createTicket(help);
        Payments payment = paymentsService.save(createPaymentRequest(ticket));
        em.flush();
        em.clear();
        return payment;
    }


    @Test
    void findAllOkTest() throws Exception
    {
        PaymentHelp help = createKey();
        createPayment(help);

        mockMvc.perform(get(API_BASE).session(session))
                .andExpect(status().isOk());
    }

    @Test
    void findAllErrorTest() throws Exception
    {
        mockMvc.perform(get(API_BASE).session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdOkTest() throws Exception
    {
        PaymentHelp help = createKey();
        Payments payment = createPayment(help);

        mockMvc.perform(get(API_BASE + "/" + payment.getId()).session(session))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdErrorTest() throws Exception
    {
        mockMvc.perform(get(API_BASE + "/-1").session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOkTest() throws Exception
    {
        PaymentHelp help = createKey();
        Tickets ticket = createTicket(help);
        PaymentsRequest request = createPaymentRequest(ticket);

        mockMvc.perform(post(API_BASE)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createErrorBadRequestTest() throws Exception
    {
        PaymentHelp help = createKey();
        Tickets ticket = createTicket(help);

        String json = """
                {
                  "amount": 33.50,
                  "method": "CARD",
                  "status": "PAID",
                  "timestamp": "not-a-date",
                  "ticket_id": %d
                }
                """.formatted(ticket.getId());

        mockMvc.perform(post(API_BASE)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError());
    }
}



class PaymentHelp
{
    private final Users user;
    private final Events event;
    private final TicketKategorie ticketKategorie;
    private final ExistingElementKey2 elementKey;

    public PaymentHelp(Users user, Events event, TicketKategorie ticketKategorie, ExistingElementKey2 elementKey)
    {
        this.user = user;
        this.event = event;
        this.ticketKategorie = ticketKategorie;
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

    public ExistingElementKey2 getElementKey()
    {
        return elementKey;
    }
}

class ExistingElementKey2
{
    private final Long seatPlanId;
    private final Integer x;
    private final Integer y;

    public ExistingElementKey2(Long seatPlanId, Integer x, Integer y)
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