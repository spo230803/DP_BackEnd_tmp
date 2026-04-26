package at.spengergasse.dp_backend.service.ticket;

import at.spengergasse.dp_backend.dto.ticket.TicketKategorienEventsRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.models.ticket.TicketKategorie;
import at.spengergasse.dp_backend.models.ticket.TicketKategorienEvents;
import at.spengergasse.dp_backend.models.ticket.TicketKategorienEventsId;
import at.spengergasse.dp_backend.repository.ticket.TicketKategorienEventsRespository;
import at.spengergasse.dp_backend.service.BaseService;
import at.spengergasse.dp_backend.service.event.EventsService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.LoggerNameAwareMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import at.spengergasse.dp_backend.models.*;
import at.spengergasse.dp_backend.models.ticket.TicketKategorie;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TicketKategorienEventsService extends BaseService {

    private final TicketKategorienEventsRespository ticketKategorienEventsRespository;
    private final TicketKategorieService tkService;
    private final EventsService eventsService;

    public List<TicketKategorienEvents> getAllByEventId(Long eventId) {
        if (eventId == null) throw ExeException.idNullOrBad("eventId");

        // valida esistenza event (così differenzi 404 evento vs lista vuota)
        eventsService.findById(eventId);

        return ticketKategorienEventsRespository.findByEvents_Id(eventId);
        // Se vuoi: se vuota, puoi anche decidere di ritornare List.of() invece di 404
    }

    public TicketKategorienEvents getByEventIdAndTicketKategorieId(Long eventId, Long ticketKategorieId) {
        if (eventId == null) throw ExeException.idNullOrBad("eventId");
        if (ticketKategorieId == null) throw ExeException.idNullOrBad("ticketKategorieId");

        // valida evento
        eventsService.findById(eventId);

        return ticketKategorienEventsRespository
                .findByEvents_IdAndTicketKategorie_Id(eventId, ticketKategorieId)
                .orElseThrow(() -> ExeException.notFound(
                        "TicketKategorienEvents (eventId=" + eventId + ", ticketKategorieId=" + ticketKategorieId + ")"
                ));
    }

    /**
     * Aggiunge una TicketKategorie ad un Event (relazione).
     * eventId lo prendiamo dall'URL -> evita incoerenze col body.
     */
    public TicketKategorienEvents addTicketKategorieToEvent(Long eventId,
                                                            @Valid TicketKategorienEventsRequest req) throws ExeException {
        if (eventId == null) throw ExeException.idNullOrBad("eventId");
        if (req == null) throw ExeException.idNullOrBad("request");

        log.debug("Add TicketKategorie to Event: eventId={}, req={}", eventId, req);

        Events event = eventsService.findById(eventId);
        TicketKategorie ticketKategorie = tkService.findById(req.ticketKategorienId());

        // evita duplicati (se già esiste la relazione -> 409)
        boolean exists = ticketKategorienEventsRespository
                .findByEvents_IdAndTicketKategorie_Id(eventId, req.ticketKategorienId())
                .isPresent();
        if (exists) {
            throw ExeException.ofConflict("Relazione già esistente (eventId=" + eventId +
                    ", ticketKategorieId=" + req.ticketKategorienId() + ")");
        }

        TicketKategorienEvents entity = new TicketKategorienEvents(
                event,
                ticketKategorie,
                req.price()
        );

        TicketKategorienEvents saved = ticketKategorienEventsRespository.save(entity);
        log.info("Created TicketKategorienEvents={}", saved);
        return saved;
    }

    public void deleteByEventIdAndTicketKategorieId(Long eventId, Long ticketKategorieId) {
        if (eventId == null) throw ExeException.idNullOrBad("eventId");
        if (ticketKategorieId == null) throw ExeException.idNullOrBad("ticketKategorieId");

        TicketKategorienEventsId id = new TicketKategorienEventsId(eventId, ticketKategorieId);

        if (!ticketKategorienEventsRespository.existsById(id)) {
            throw ExeException.notFound(
                    "TicketKategorienEvents (eventId=" + eventId + ", ticketKategorieId=" + ticketKategorieId + ")"
            );
        }

        ticketKategorienEventsRespository.deleteById(id);
    }

    @Transactional
    public TicketKategorienEvents update(Long eventId,
                                         Long ticketKategorieId,
                                         @Valid TicketKategorienEventsRequest request) {

        if (eventId == null) throw ExeException.idNullOrBad("eventId");
        if (ticketKategorieId == null) throw ExeException.idNullOrBad("ticketKategorieId");

        TicketKategorienEvents entity =
                ticketKategorienEventsRespository
                        .findByEvents_IdAndTicketKategorie_Id(eventId, ticketKategorieId)
                        .orElseThrow(() ->
                                ExeException.notFound(
                                        "TicketKategorienEvents (eventId=" + eventId +
                                                ", ticketKategorieId=" + ticketKategorieId + ")"
                                )
                        );

        // aggiorniamo solo il prezzo (tipicamente unico campo modificabile)
        TicketKategorienEventsId id = new TicketKategorienEventsId(eventId , ticketKategorieId);

        entity.setPrice(request.price());
        entity.setId(id);

        return ticketKategorienEventsRespository.save(entity);
    }
}
