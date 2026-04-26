package at.spengergasse.dp_backend.controller.users;

// API für Teilnehmer

import at.spengergasse.dp_backend.controller.BaseController;
import at.spengergasse.dp_backend.dto.query.TicketsParticipants;
import at.spengergasse.dp_backend.repository.ticket.TicketsRepository;
import at.spengergasse.dp_backend.service.ticket.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Participants")
public class ApiParticipants extends BaseController
{

    @Autowired
    private TicketService ticketService;

    /**
     * Alle Teilnehmer (mit Ticket) einladen, auch diejenigen, die noch nicht bezahlt haben.
     * @param id (event)
     * @return (list von alles Participants)
     */
    @GetMapping("/getAllByEvent/{id}")
    public ResponseEntity<List<TicketsParticipants>> getAllByEvent(@PathVariable Long id)
    {
        return ResponseEntity.ok(ticketService.findTicketUserSeatByEventId(id));
    }

    @PostMapping("/{ticketId}")
    public ResponseEntity<TicketsParticipants> updateTicketsStatus(
            @PathVariable Long ticketId,
            @RequestBody @Valid TicketsParticipants ticketsParticipants
    ) {
        return ResponseEntity.ok(
                ticketService.updateTicketStatus(ticketId, ticketsParticipants)
        );
    }
}//end Class
