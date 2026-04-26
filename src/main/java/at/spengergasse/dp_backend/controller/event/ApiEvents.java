package at.spengergasse.dp_backend.controller.event;

import at.spengergasse.dp_backend.controller.BaseController;
import at.spengergasse.dp_backend.dto.event.EventsRequest;
import at.spengergasse.dp_backend.models.enums.EventStatus;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.models.event.SeatPlans;
import at.spengergasse.dp_backend.service.event.EventsService;
import at.spengergasse.dp_backend.service.event.SeatPlansService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Events")
@Slf4j
public class ApiEvents extends BaseController
{
    private final  EventsService eventsService;
    private final     SeatPlansService seatPlansService;

    @GetMapping()
    public ResponseEntity<List<Events>> getAllEvents()
    {
        return ResponseEntity.ok().body(eventsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Events> getEventById(@PathVariable Long id)
    {
        return ResponseEntity.ok().body(eventsService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Events> deleteEventById(@PathVariable Long id)
    {
        eventsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<Events> createEvent(@Valid @RequestBody EventsRequest event)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventsService.save(event)) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Events> updateEvent(@PathVariable Long id, @Valid @RequestBody EventsRequest event)
    {
        return ResponseEntity.ok(eventsService.update(id, event));
    }
    

}//end class