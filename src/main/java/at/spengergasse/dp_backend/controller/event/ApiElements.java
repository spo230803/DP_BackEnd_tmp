package at.spengergasse.dp_backend.controller.event;

import at.spengergasse.dp_backend.controller.BaseController;
import at.spengergasse.dp_backend.dto.Seatplan.Element;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.event.Elements;
import at.spengergasse.dp_backend.service.event.ElementsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/elements")
public class ApiElements extends BaseController {

    private final ElementsService elementsService;

    // =============================
    // CREATE
    // =============================

    @PostMapping
    public ResponseEntity<Element> create(@RequestBody @Valid Element request) throws ExeException {

        Elements element = elementsService.createElement(
                request.seatPlanId(),
                request.x(),
                request.y(),
                request.type(),
                request.label(),
                request.row(),
                request.number()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToDto(element));
    }

    // =============================
    // GET ALL BY SEAT PLAN
    // =============================

    @GetMapping("/seatplan/{seatPlanId}")
    public List<Element> getBySeatPlan(@PathVariable Long seatPlanId) {

        return elementsService.getBySeatPlan(seatPlanId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // =============================
    // GET ONE
    // =============================

    @GetMapping("/{seatPlanId}/{x}/{y}")
    public Element getOne(@PathVariable Long seatPlanId,
                          @PathVariable Integer x,
                          @PathVariable Integer y) throws ExeException {

        return mapToDto(
                elementsService.getOne(seatPlanId, x, y)
        );
    }

    // =============================
    // UPDATE AVAILABILITY
    // =============================

    @PatchMapping("/{seatPlanId}/{x}/{y}/availability")
    public Element updateAvailability(@PathVariable Long seatPlanId,
                                      @PathVariable Integer x,
                                      @PathVariable Integer y,
                                      @RequestParam boolean available) throws ExeException {

        return mapToDto(
                elementsService.updateAvailability(seatPlanId, x, y, available)
        );
    }

    // =============================
    // DELETE ONE
    // =============================

    @DeleteMapping("/{seatPlanId}/{x}/{y}")
    public ResponseEntity<Void> delete(@PathVariable Long seatPlanId,
                                       @PathVariable Integer x,
                                       @PathVariable Integer y) {

        elementsService.deleteOne(seatPlanId, x, y);
        return ResponseEntity.noContent().build();
    }

    // =============================
    // MAPPER
    // =============================

    private Element mapToDto(Elements e) {
        return new Element(
                e.getId().getSeatPlanId(),
                e.getId().getX(),
                e.getId().getY(),
                e.getType(),
                e.getLabel(),
                e.isAvailable(),
                e.getRow(),
                e.getNumber()
        );
    }
}