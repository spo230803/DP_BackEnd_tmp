package at.spengergasse.dp_backend.controller.event;

import at.spengergasse.dp_backend.controller.BaseController;
import at.spengergasse.dp_backend.dto.Seatplan.SeatPlanElementsRequest;
import at.spengergasse.dp_backend.dto.event.SeatPlansRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.models.event.SeatPlans;
import at.spengergasse.dp_backend.service.event.EventsService;
import at.spengergasse.dp_backend.service.event.SeatPlansService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/SeatPlans")
@RequestMapping("/api/Events/{eventId}/SeatPlans")
public class ApiSeatPlans extends BaseController
{
    private final SeatPlansService seatPlansService;
    private final EventsService eventsService;

    @GetMapping
    public ResponseEntity<SeatPlans> get(@PathVariable Long eventId) {
        eventsService.findById(eventId); // valida che esista l'evento
        return ResponseEntity.ok(seatPlansService.findByEventId(eventId)); // ritorna 1 seatplan
    }

    @PostMapping
    public ResponseEntity<SeatPlans> create(@PathVariable Long eventId,
                                            @Valid @RequestBody SeatPlansRequest request) {
        eventsService.findById(eventId);
        SeatPlans created = seatPlansService.createForEvent(eventId, request); // deve fallire se già esiste
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping
    public ResponseEntity<SeatPlans> upsert(@PathVariable Long eventId,
                                            @Valid @RequestBody SeatPlansRequest request) {
        eventsService.findById(eventId);
        SeatPlans saved = seatPlansService.upsertForEvent(eventId, request);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable Long eventId) {
        seatPlansService.deleteByEventId(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/elements")
    public ResponseEntity<?> getElements(@PathVariable Long eventId) {
        return ResponseEntity.ok(seatPlansService.loadLayoutByEventId(eventId));
    }

    @PutMapping("/elements")
    public ResponseEntity<Void> saveElements(@PathVariable Long eventId,
                                             @RequestBody SeatPlanElementsRequest req) {
        seatPlansService.saveLayoutByEventId(eventId, req.getElements());
        return ResponseEntity.ok().build();
    }

}//end Class
