package at.spengergasse.dp_backend.controller.ticket;

import at.spengergasse.dp_backend.controller.BaseController;
import at.spengergasse.dp_backend.dto.ticket.TicketKategorieRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.ticket.TicketKategorie;
import at.spengergasse.dp_backend.service.ticket.TicketKategorieService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/TicketKategorie")
public class ApiTicketKategorie extends BaseController
{

    private final TicketKategorieService   ticketKategorieService;

    @GetMapping()
    public ResponseEntity<List<TicketKategorie>> getAll()
    {
        List<TicketKategorie> list = ticketKategorieService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketKategorie> getById(@PathVariable("id") Long id)
    {
        TicketKategorie ticketKategorie = ticketKategorieService.findById(id);
        return ResponseEntity.ok(ticketKategorie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object>  delete(@PathVariable Long id)
    {
        ticketKategorieService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody TicketKategorieRequest request)
    {
        TicketKategorie newTK =  ticketKategorieService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TicketKategorie> update(@PathVariable Long id,
                                                   @RequestBody TicketKategorieRequest request) throws ExeException {
        TicketKategorie updated = ticketKategorieService.update(id, request);
        return ResponseEntity.ok(updated);
    }

}//end Class
