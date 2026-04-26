package at.spengergasse.dp_backend.controller.ticket;

import at.spengergasse.dp_backend.controller.BaseController;
import at.spengergasse.dp_backend.dto.ticket.TicketsRequest;
import at.spengergasse.dp_backend.dto.ticket.TicketsResponse;
import at.spengergasse.dp_backend.models.ticket.Tickets;
import at.spengergasse.dp_backend.service.system.EmailService;
import at.spengergasse.dp_backend.service.ticket.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Tickets")
public class ApiTicket extends BaseController
{

    private final TicketService ticketService;


    @GetMapping("/getAll")
    public ResponseEntity<List<TicketsResponse>> getAll()
    {
        List<Tickets> tickets = ticketService.findAll();
        List<TicketsResponse> result = tickets.stream().map(TicketsResponse::from).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TicketsResponse> getById(@PathVariable Long id)
    {
        Tickets ticket = ticketService.findById(id);
        return ResponseEntity.ok(TicketsResponse.from(ticket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id)
    {
        ticketService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping()
    public ResponseEntity<TicketsResponse> create(@Valid @RequestBody TicketsRequest request)
    {
        Tickets newTicket = ticketService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(TicketsResponse.from(newTicket));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketsResponse> update(@PathVariable Long id,
                                          @Valid @RequestBody TicketsRequest request) {
        Tickets updated = ticketService.update(id, request);
        return ResponseEntity.ok(TicketsResponse.from(updated));
    }
}//end class
