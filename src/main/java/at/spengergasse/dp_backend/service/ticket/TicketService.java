package at.spengergasse.dp_backend.service.ticket;

import at.spengergasse.dp_backend.GlobalConstat;
import at.spengergasse.dp_backend.dto.EmailModel.TicketKaufen;
import at.spengergasse.dp_backend.dto.query.TicketsNotPaid;
import at.spengergasse.dp_backend.dto.query.TicketsParticipants;
import at.spengergasse.dp_backend.dto.ticket.TicketsRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.event.Elements;
import at.spengergasse.dp_backend.models.event.ElementsId;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.models.ticket.Discounts;
import at.spengergasse.dp_backend.models.ticket.Payments;
import at.spengergasse.dp_backend.models.ticket.TicketKategorie;
import at.spengergasse.dp_backend.models.ticket.Tickets;
import at.spengergasse.dp_backend.models.users.Users;
import at.spengergasse.dp_backend.repository.event.ElementsRepository;
import at.spengergasse.dp_backend.repository.ticket.DiscountsRepository;
import at.spengergasse.dp_backend.repository.ticket.PaymentsRepository;
import at.spengergasse.dp_backend.repository.ticket.TicketsRepository;
import at.spengergasse.dp_backend.service.BaseService;
import at.spengergasse.dp_backend.service.event.EventsService;
import at.spengergasse.dp_backend.service.system.EmailService;
import at.spengergasse.dp_backend.service.users.UsersService;
import at.spengergasse.dp_backend.system.emailModel.EmailModelNotPaid;
import at.spengergasse.dp_backend.system.emailModel.EmailModelTicketKaufen;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class TicketService extends BaseService {

    private final TicketsRepository ticketsRepository;
    private final EventsService eventsService;
    private final DiscountsRepository discountsRepository;
    private final UsersService usersService;
    private final TicketKategorieService ticketKategorieService;
    private final ElementsRepository elementsRepository;
    private final PaymentsRepository paymentsRepository;


    private final EmailModelTicketKaufen emailModelTicketKaufen;

    public TicketService(
            TicketsRepository ticketsRepository,
            EventsService eventsService,
            DiscountsRepository discountsRepository,
            UsersService usersService,
            TicketKategorieService ticketKategorieService,
            ElementsRepository elementsRepository, PaymentsRepository paymentsRepository,
            EmailModelTicketKaufen emailModelTicketKaufen
    ) {
        this.ticketsRepository = ticketsRepository;
        this.eventsService = eventsService;
        this.discountsRepository = discountsRepository;
        this.usersService = usersService;
        this.ticketKategorieService = ticketKategorieService;
        this.elementsRepository = elementsRepository;
        this.paymentsRepository = paymentsRepository;
        this.emailModelTicketKaufen = emailModelTicketKaufen;
        this.globalConstat = new GlobalConstat();
    }

    public List<Tickets> findAll() {
        List<Tickets> list = ticketsRepository.findAll();
        if (list.isEmpty()) {
            throw ExeException.notFound("Tickets");
        }
        return list;
    }

    public List<Tickets> findAllByDiscount(Long discountId) {
        if (discountId == null) throw ExeException.idNullOrBad("discountId is null");
        return ticketsRepository.findByDiscount_Id(discountId);
    }

    public Tickets findById(Long id) {
        return ticketsRepository.findById(id)
                .orElseThrow(() -> ExeException.notFound("Ticket", id));
    }

    public List<TicketsParticipants> findTicketUserSeatByEventId(Long eventId) {
        if(eventId == null) throw ExeException.idNullOrBad("Id Event");
        List<TicketsParticipants> list = ticketsRepository.findTicketUserSeatByEventId(eventId);
        if(list.isEmpty()){
            throw ExeException.notFound("Id Event", eventId);
        }
        return list;
    }

    public void delete(Long id) {
        Tickets ticket = findById(id);
        ticketsRepository.delete(ticket);
    }

    public Tickets save(Tickets ticket) {
        if (ticket == null) throw ExeException.idNullOrBad("ticket is null");
        return ticketsRepository.save(ticket);
    }

    /**
     * Crea un ticket a partire dal DTO.
     * - discount è opzionale
     * - element è FK composta (seat_plan_id,row,number)
     */
    public Tickets save(@Valid TicketsRequest request) throws ExeException {
        try {
            if (request == null) throw  ExeException.idNullOrBad("request is null");

            Users user = usersService.findById(request.userId());
            Events event = eventsService.findById(request.eventId());
            TicketKategorie ticketKategorie = ticketKategorieService.findById(request.ticketKategorieId());

            // discount opzionale
            Discounts discount = null;
            if (request.discountId() != null) {
                discount = discountsRepository.findById(request.discountId())
                        .orElseThrow(() -> ExeException.notFound("Discount", request.discountId()));
            }

            // element FK composta
            ElementsId elementsId = new ElementsId(
                    request.elementsSeatPlanId(),
                    request.elementsX(),
                    request.elementsY()
            );

            Elements element = elementsRepository.findById(elementsId)
                    .orElseThrow(() -> ExeException.notFound(
                            "Element not found: seatPlanId=" + request.elementsSeatPlanId()
                                    + ", x=" + request.elementsX()
                                    + ", y=" + request.elementsY()
                    ));

            Tickets newTicket = new Tickets(
                    request.status(),
                    user,
                    event,
                    discount,
                    ticketKategorie,
                    request.price(),
                    element
            );

            //----------  EMAIL ----------------
            TicketKaufen[] ticketKaufen = new TicketKaufen[]{new TicketKaufen(
                    user.getEmail(),
                    user.getName(),
                    event.getTitle(),
                    event.getIban(),
                    newTicket.getPrice()
            )};

            emailModelTicketKaufen.sendEmail(ticketKaufen);

            return save(newTicket);

        }  catch (Throwable e) {
            throw ExeException.ofInternalError(e.getMessage());
        }
    }

    public List<TicketsNotPaid> findTiketNotPaid() {
        return ticketsRepository.findTiketNotPaid();
    }

    @Transactional
    public Tickets update(@NotNull Long id, @Valid TicketsRequest request) {

        if (id == null) {
            throw ExeException.idNullOrBad("Ticket");
        }

        Tickets ticket = ticketsRepository.findById(id)
                .orElseThrow(() -> ExeException.notFound("Ticket", id));

        // aggiorna solo i campi modificabili
        ticket.setStatus(request.status());
        ticket.setPrice(request.price());
        ticket.setId(id);

        return ticketsRepository.save(ticket);
    }

    @Transactional
    public TicketsParticipants updateTicketStatus(Long id, @Valid TicketsParticipants ticketsParticipants) {
        Tickets updateTicket = ticketsRepository.findById(id)
                .orElseThrow(() -> ExeException.notFound("Ticket", id));

        updateTicket.setStatus(ticketsParticipants.ticketStatus());
        updateTicket = ticketsRepository.save(updateTicket);

        // Payment nur anlegen, wenn Ticket wirklich bezahlt wurde
        if ("PAID".equalsIgnoreCase(updateTicket.getStatus())) {
            Payments paid = new Payments(
                    updateTicket.getPrice(),
                    "MANUEL",
                    updateTicket.getStatus(),
                    LocalDateTime.now(),
                    updateTicket
            );
            paymentsRepository.save(paid);
        }

        return new TicketsParticipants(
                updateTicket.getId(),
                updateTicket.getUser().getName(),
                updateTicket.getUser().getEmail(),
                updateTicket.getElement() != null ? updateTicket.getElement().getLabel() : null,
                updateTicket.getPrice(),
                updateTicket.getStatus()
        );
    }
}//End class