package at.spengergasse.dp_backend.service.ticket;

import at.spengergasse.dp_backend.dto.ticket.PaymentsRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.ticket.Payments;
import at.spengergasse.dp_backend.models.ticket.Tickets;
import at.spengergasse.dp_backend.repository.ticket.PaymentsRepository;
import at.spengergasse.dp_backend.repository.ticket.TicketsRepository;
import at.spengergasse.dp_backend.service.BaseService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentsService extends BaseService
{
    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private TicketService ticketService;


    public Payments save(Payments payments)
    {
        if (payments == null) throw ExeException.idNullOrBad("Payments is null");
        return paymentsRepository.save(payments);
    }

    public Payments save(@NotNull @Valid PaymentsRequest request)
    {
        Tickets ticket = ticketService.findById(request.ticketId());

        return  save(new Payments(
                request.amount(),
                request.method(),
                request.status(),
                request.timestamp(),
                ticket
        ));
    }

    public List<Payments> getAll()
    {
        List<Payments> listt = paymentsRepository.findAll();
        if (listt.isEmpty()){ throw ExeException.notFound("Payments");}
        return listt;
    }

    public Payments getByIdTicket(Long idTicket)
    {
        Optional<Payments> payment = Optional.of(paymentsRepository.findByTicket_Id(idTicket)
                .orElseThrow(() -> ExeException.notFound("Whit Ticket ID ", idTicket)
                ));
        return  payment.get();
    }

    public Payments getById(Long id)
    {
        if(id == null) { throw ExeException.idNullOrBad("Payments");}
        Optional<Payments> payment = Optional.of(paymentsRepository.findById(id)
                .orElseThrow(() -> ExeException.notFound("Payames", id)));
        return payment.get();

    }
}
