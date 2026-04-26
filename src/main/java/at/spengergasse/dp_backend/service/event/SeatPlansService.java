package at.spengergasse.dp_backend.service.event;

import at.spengergasse.dp_backend.dto.Seatplan.Element;
import at.spengergasse.dp_backend.dto.event.SeatPlansRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.enums.ElementType;
import at.spengergasse.dp_backend.models.event.Elements;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.models.event.SeatPlans;
import at.spengergasse.dp_backend.repository.event.ElementsRepository;
import at.spengergasse.dp_backend.repository.event.SeatPlansRepository;
import at.spengergasse.dp_backend.service.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatPlansService extends BaseService
{
    private final SeatPlansRepository seatPlansRepository;
    private final EventsService eventsService;
    private final ObjectMapper objectMapper;
    private final ElementsRepository elementsRepository;

    // =============================
    // FIND (1:1) - by eventId
    // =============================

    public SeatPlans getByEventId(@NotNull Long eventId) throws ExeException {
        if (eventId == null) throw ExeException.idNullOrBad("Event");

        // se Event non esiste -> 404
        eventsService.findById(eventId);

        return seatPlansRepository.findByEventId(eventId)
                .orElseThrow(() -> ExeException.notFound("SeatPlans for Event", eventId));
    }

    /**
     * Se non esiste, ne crea uno "di default" (ma NON lo salva).
     * Utile se vuoi far vedere il seatplan anche quando è vuoto.
     */
    public SeatPlans getOrBuildDefaultByEventId(@NotNull Long eventId) throws ExeException {
        if (eventId == null) throw ExeException.idNullOrBad("Event");

        Events event = eventsService.findById(eventId);

        return seatPlansRepository.findByEventId(eventId)
                .orElseGet(() -> new SeatPlans(event.getTitle(), event));
    }

    public SeatPlans findById(@NotNull Long id) throws ExeException {
        if (id == null) throw ExeException.idNullOrBad("SeatPlans");

        return seatPlansRepository.findById(id)
                .orElseThrow(() -> ExeException.notFound("SeatPlans", id));
    }

    // =============================
    // CREATE / UPSERT (1:1)
    // =============================

    /** Crea SOLO se non esiste già -> altrimenti 409 */
    public SeatPlans createForEvent(@NotNull Long eventId, @Valid SeatPlansRequest request) throws ExeException {
        if (eventId == null) throw ExeException.idNullOrBad("Event");
        Events event = eventsService.findById(eventId);

        if (seatPlansRepository.existsByEventId(eventId)) {
            throw ExeException.ofConflict("SeatPlan existire mit eventId=" + eventId);
        }

        SeatPlans seatPlan = new SeatPlans(request.getName(), event);
        return save(seatPlan);
    }

    /** Crea se non esiste, altrimenti aggiorna (es. nome) */
    public SeatPlans upsertForEvent(@NotNull Long eventId, @Valid SeatPlansRequest request) throws ExeException {
        if (eventId == null) throw ExeException.idNullOrBad("Event");
        Events event = eventsService.findById(eventId);

        SeatPlans seatPlan = seatPlansRepository.findByEventId(eventId)
                .orElseGet(() -> new SeatPlans(request.getName(), event));

        seatPlan.setName(request.getName());

        return save(seatPlan);
    }

    public SeatPlans save(@NotNull SeatPlans seatPlans) throws ExeException {
        try {
            return seatPlansRepository.save(seatPlans);
        } catch (Throwable e) {
            throw ExeException.ofBadRequest(e.getMessage());
        }
    }

    // =============================
    // DELETE
    // =============================

    /** Cancella seatplan dell’evento (non per id generico) */
    public void deleteByEventId(@NotNull Long eventId) throws ExeException {
        if (eventId == null) throw ExeException.idNullOrBad("Event");
        // se non esiste -> 404
        SeatPlans sp = getByEventId(eventId);
        seatPlansRepository.delete(sp);
    }

    // =============================
    // LAYOUT JSON
    // =============================

    public void saveLayoutByEventId(@NotNull Long eventId, List<Element> elements) throws ExeException {
        try {
            SeatPlans seatPlan = getByEventId(eventId);
            List<Element> safeElements = elements == null ? List.of() : elements;
            String json = objectMapper.writeValueAsString(safeElements);

            seatPlan.setElementsJson(json);
            seatPlansRepository.save(seatPlan);

            // Sync elements table (used by TicketService for seat validation)
            List<Elements> existing = elementsRepository.findBySeatPlan_Id(seatPlan.getId());
            var newKeys = new java.util.HashSet<String>();

            for (Element el : safeElements) {
                if (el.type() == ElementType.SEAT) {
                    newKeys.add(seatPlan.getId() + ":" + el.x() + ":" + el.y());
                    Elements entity = new Elements(
                            seatPlan,
                            el.type(),
                            el.label(),
                            el.x(),
                            el.y(),
                            el.row(),
                            el.number()
                    );
                    elementsRepository.save(entity);
                }
            }

            // Remove elements no longer in layout (skip if referenced by tickets)
            for (Elements old : existing) {
                String key = old.getId().getSeatPlanId() + ":" + old.getId().getX() + ":" + old.getId().getY();
                if (!newKeys.contains(key)) {
                    try {
                        elementsRepository.delete(old);
                        elementsRepository.flush();
                    } catch (Exception ignored) {
                        // FK constraint — element is referenced by a ticket, keep it
                    }
                }
            }
        } catch (ExeException e) {
            throw e;
        } catch (Exception e) {
            throw ExeException.ofBadRequest(e.getMessage());
        }
    }

    public List<Element> loadLayoutByEventId(@NotNull Long eventId) throws ExeException {
        try {
            SeatPlans seatPlan = getByEventId(eventId);
            String json = seatPlan.getElementsJson();

            if (json == null || json.isBlank()) return List.of();

            JavaType type = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, Element.class);

            return objectMapper.readValue(json, type);
        } catch (ExeException e) {
            throw e;
        } catch (Exception e) {
            throw ExeException.ofBadRequest(e.getMessage());
        }
    }

    public SeatPlans findByEventId(@NotNull Long eventId) throws ExeException {

        if (eventId == null) {
            throw ExeException.idNullOrBad("eventId");
        }

        // 1️⃣ Verifico che l'evento esista
        eventsService.findById(eventId);

        // 2️⃣ Cerco il SeatPlan
        return seatPlansRepository.findByEventId(eventId)
                .orElseThrow(() ->
                        ExeException.notFound("SeatPlans for eventId=" + eventId)
                );
    }
}//end Class