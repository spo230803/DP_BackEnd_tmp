package at.spengergasse.dp_backend.controller.ticket;

import at.spengergasse.dp_backend.controller.BaseController;
import at.spengergasse.dp_backend.dto.ticket.PaymentsRequest;
import at.spengergasse.dp_backend.models.ticket.Payments;
import at.spengergasse.dp_backend.repository.ticket.PaymentsRepository;
import at.spengergasse.dp_backend.service.ticket.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Payments")
public class ApiPayments  extends BaseController
{
    private final PaymentsService paymentsService;

    @GetMapping()
    public ResponseEntity<List<PaymentsRequest>> getAll()
    {
        List<Payments> payments = paymentsService.getAll();
        List<PaymentsRequest> retult = payments.stream().map(PaymentsRequest::from).toList();
        return ResponseEntity.ok(retult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payments> getById(@PathVariable("id") Long id )
    {
        Payments payment =paymentsService.getById(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/Ticket/{id}")
    public ResponseEntity<Payments> getByIdTicket(@PathVariable Long  idTicket, @PathVariable String id)
    {
        Payments payment = paymentsService.getByIdTicket(idTicket);
        return ResponseEntity.ok(payment);
    }

    @PostMapping()
    public ResponseEntity<Payments> create(@RequestBody PaymentsRequest request)
    {
        Payments newPayment = paymentsService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPayment);
    }
}
