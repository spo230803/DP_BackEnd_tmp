package at.spengergasse.dp_backend.service.event;

import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.enums.ElementType;
import at.spengergasse.dp_backend.models.event.Elements;
import at.spengergasse.dp_backend.models.event.ElementsId;
import at.spengergasse.dp_backend.models.event.SeatPlans;
import at.spengergasse.dp_backend.repository.event.ElementsRepository;
import at.spengergasse.dp_backend.repository.event.SeatPlansRepository;
import at.spengergasse.dp_backend.service.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import at.spengergasse.dp_backend.dto.Seatplan.Element;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ElementsService extends BaseService {


    private final ElementsRepository elementsRepository;
    private final SeatPlansRepository seatPlansRepository;

    public ElementsService(ElementsRepository elementsRepository,
                           SeatPlansRepository seatPlansRepository) {
        this.elementsRepository = elementsRepository;
        this.seatPlansRepository = seatPlansRepository;
    }

    // =============================
    // CREATE
    // =============================

    public Elements createElement(Long seatPlanId,
                                  Integer x,
                                  Integer y,
                                  ElementType type,
                                  String label,
                                  String row,
                                  Integer number) throws ExeException {

        SeatPlans seatPlan = seatPlansRepository.findById(seatPlanId)
                .orElseThrow(() ->  ExeException.notFound("SeatPlan",seatPlanId));

        ElementsId id = new ElementsId(seatPlanId, x, y);

        if (elementsRepository.existsById(id)) {
            ExeException.notFound("Element already exists");
        }

        Elements element = new Elements(seatPlan, type, label, x, y, row, number );

        return elementsRepository.save(element);
    }

    // =============================
    // READ
    // =============================

    @Transactional
    public List<Elements> getBySeatPlan(Long seatPlanId) {
        return elementsRepository.findBySeatPlan_Id(seatPlanId);
    }

    @Transactional()
    public Elements getOne(Long seatPlanId, Integer x, Integer y) throws ExeException {
        ElementsId id = new ElementsId(seatPlanId, x, y);

        return elementsRepository.findById(id)
                .orElseThrow(() ->  ExeException.notFound("Element", id.getSeatPlanId()));
    }

    // =============================
    // UPDATE
    // =============================

    public Elements updateLabel(Long seatPlanId,
                                Integer x,
                                Integer y,
                                String newLabel) throws ExeException {

        Elements element = getOne(seatPlanId, x, y);

        element.setLabel(newLabel);

        return elementsRepository.save(element);
    }

    public Elements updateAvailability(Long seatPlanId,
                                       Integer x,
                                       Integer y,
                                       boolean available) throws ExeException {

        Elements element = getOne(seatPlanId, x, y);

        element.setAvailable(available);

        return elementsRepository.save(element);
    }

    // =============================
    // DELETE
    // =============================

    public void deleteOne(Long seatPlanId,
                          Integer x,
                          Integer y) {

        ElementsId id = new ElementsId(seatPlanId, x, y);
        elementsRepository.deleteById(id);
    }

    public long deleteBySeatPlan(Long seatPlanId) {
        return elementsRepository.deleteById_SeatPlanId(seatPlanId);
    }


    /**
     * Früher, um Plätze freizumachen, die kein Ticket hatten
     * @return
     */
    public int freeSeatCronJob()
    {
        return elementsRepository.markFreeSeatsAsUnavailable();
    }


    public Elements save(Elements element)
    {
        if(element == null){
            throw ExeException.idNullOrBad("Element");
        }
        return elementsRepository.save(element);
    }


    /*
    public List<at.spengergasse.dp_backend.dto.Seatplan.Element> loadElements(Long seatPlanId)
            throws ExeException {

        seatPlansService.findById(seatPlanId); // Validierung

        var entities = elementRepository.findBySeatPlan_Id(seatPlanId);

        List<Element> result = new ArrayList<>();

        for (var el : entities) {

            var dto = new Element();
            dto.setId(String.valueOf(el.getId()));
            dto.setType(el.getType().name());
            dto.setLabel(el.getLabel());
            dto.setX(el.getX());
            dto.setY(el.getY());

            if (el.getType() == ElementType.SEAT) {

                Seats seat = seatsRepository.findByElement_Id(el.getId()).orElse(null);

                if (seat != null) {
                    dto.setRow(seat.getRow());
                    dto.setNumber(seat.getNumber());
                    dto.setAvailable(seat.isAvailable());
                }
            }

            result.add(dto);
        }

        return result;
    }

     */

}
