package at.spengergasse.dp_backend.service.event;

import at.spengergasse.dp_backend.dto.event.EventsRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.enums.EventStatus;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.repository.event.EventsRepository;
import at.spengergasse.dp_backend.service.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class EventsService extends BaseService
{
    @Autowired
    EventsRepository eventsRepository;

    public List<Events>findAll() throws ExeException
    {
        List<Events> list = eventsRepository.findAll();
        if (list.isEmpty()) {
            return list;
            //throw ExeException.notFound("Events not found");
        }
        return list.stream()
                .filter(events -> events.getStatus() != EventStatus.CANCEL)
                .toList();
    }

    public Events findById(Long id) throws ExeException
    {
        if(id == null) {
            throw ExeException.idNullOrBad("Event");
        }
        Optional<Events> event = Optional.of(eventsRepository.findById(id).orElseThrow(
                () -> {
                    throw ExeException.notFound("Event", id);
                }
        ));
        if(event.get().getStatus() == EventStatus.CANCEL){
            throw ExeException.notFound("Event", id);
        }
        return event.get();
    }

    @Transactional
    public Events save(Events event) throws ExeException
    {
        if(event == null) {
            throw ExeException.ofBadRequest("Events");
        }
        return  eventsRepository.save(event);
    }

    @Transactional
    public Events save(@Valid EventsRequest evet) throws ExeException
    {
        Events newEvent = new Events(
                evet.title(),
                evet.subtitle(),
                evet.startsDate(),
                evet.endsDate(),
                evet.location(),
                evet.iban(),
                evet.status()
        );
        return save(newEvent);
    }

    @Transactional
    public void deleteById(Long id) throws ExeException
    {
        if(id == null) {
            throw ExeException.idNullOrBad("Events");
        }
        Events event = eventsRepository.findById(id).orElseThrow(() -> {
            throw ExeException.notFound("Event", id);
        });
        event.setStatus(EventStatus.CANCEL);
        eventsRepository.save(event);
    }

    @Transactional
    public Events update(@NotNull Long id, @Valid EventsRequest evet) throws ExeException
    {
        if(id == null){
            throw ExeException.idNullOrBad("Event Update");
        }
        Events event = eventsRepository.findById(id).orElseThrow(() -> {
            throw ExeException.notFound("Event", id);
        });
        Events updateEvent = new Events(
                evet.title(),
                evet.subtitle(),
                evet.startsDate(),
                evet.endsDate(),
                evet.location(),
                evet.iban(),
                evet.status()
        );

        updateEvent.setId(id);
        return eventsRepository.save(updateEvent);
    }
}//end Class