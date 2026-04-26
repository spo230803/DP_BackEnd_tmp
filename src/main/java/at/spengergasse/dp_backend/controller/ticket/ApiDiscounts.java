package at.spengergasse.dp_backend.controller.ticket;

import at.spengergasse.dp_backend.controller.BaseController;
import at.spengergasse.dp_backend.dto.ticket.DiscountsRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.ticket.Discounts;
import at.spengergasse.dp_backend.service.ticket.DiscountsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.rmi.server.RemoteRef;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Discounts")
public class ApiDiscounts extends BaseController
{
    private final DiscountsService  discountsService;


    @GetMapping("/getAll")
    public ResponseEntity<List<Discounts>> getAll()
    {
        return ResponseEntity.ok(discountsService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Discounts> findById(@PathVariable Long id)
    {
        return ResponseEntity.ok(discountsService.findById(id));
    }

    @GetMapping("/getAllNotInUse")
    public ResponseEntity<List<Discounts>> getAllNotInUse()
    {
        List<Discounts> list =  discountsService.findAllNotInUse();
        return ResponseEntity.ok().body(list);
    }


    @GetMapping("/getNotInUser/{id}")
    public ResponseEntity<Discounts>  getNotInUser(@PathVariable Long id)
    {
        Discounts discount = discountsService.findNotInUserById(id);
        return ResponseEntity.ok().body(discount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id)
    {
        discountsService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PostMapping("/create")
    public ResponseEntity<Discounts> create(@Valid @RequestBody DiscountsRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(discountsService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Discounts> update(@PathVariable Long id,
                                            @Valid @RequestBody DiscountsRequest request) throws ExeException
    {
        return ResponseEntity.ok(discountsService.update(id, request));
    }
}//end Class
