package at.spengergasse.dp_backend.controller.ticket;

import at.spengergasse.dp_backend.dto.ticket.TicketKategorienEventsRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.models.ticket.TicketKategorienEvents;
import at.spengergasse.dp_backend.service.event.EventsService;
import at.spengergasse.dp_backend.service.ticket.TicketKategorienEventsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Events/{eventId}/TicketKategorien")
public class ApiTicketKategorienEvents {

    private final TicketKategorienEventsService ticketKategorienEventsService;
    private final EventsService eventsService;

    @GetMapping
    public ResponseEntity<List<TicketKategorienEvents>> getAll(@PathVariable Long eventId) throws ExeException {
        eventsService.findById(eventId); // verifica che l'evento esista
        return ResponseEntity.ok(ticketKategorienEventsService.getAllByEventId(eventId));
    }

    @GetMapping("/{ticketKategorieId}")
    public ResponseEntity<TicketKategorienEvents> getOne(@PathVariable Long eventId,
                                                         @PathVariable Long ticketKategorieId) throws ExeException {
        eventsService.findById(eventId);
        return ResponseEntity.ok(ticketKategorienEventsService.getByEventIdAndTicketKategorieId(eventId, ticketKategorieId));
    }

    @PostMapping
    public ResponseEntity<TicketKategorienEvents> add(@PathVariable Long eventId,
                                                      @Valid @RequestBody TicketKategorienEventsRequest request) throws ExeException {
        // forzo coerenza: l'eventId arriva dall'URL, non dal body
        TicketKategorienEvents created = ticketKategorienEventsService.addTicketKategorieToEvent(eventId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{ticketKategorieId}")
    public ResponseEntity<Void> delete(@PathVariable Long eventId,
                                       @PathVariable Long ticketKategorieId) throws ExeException {
        ticketKategorienEventsService.deleteByEventIdAndTicketKategorieId(eventId, ticketKategorieId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{ticketKategorieId}")
    public ResponseEntity<TicketKategorienEvents> update(@PathVariable Long eventId,
                                                         @PathVariable Long ticketKategorieId,
                                                         @Valid @RequestBody TicketKategorienEventsRequest request)
            throws ExeException {

        TicketKategorienEvents updated =
                ticketKategorienEventsService.update(eventId, ticketKategorieId, request);

        return ResponseEntity.ok(updated);
    }
}