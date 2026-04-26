package at.spengergasse.dp_backend.service.ticket;

import at.spengergasse.dp_backend.dto.ticket.TicketKategorieRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.ticket.TicketKategorie;
import at.spengergasse.dp_backend.repository.ticket.TicketKategorieRepository;
import at.spengergasse.dp_backend.service.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketKategorieService extends BaseService
{
    @Autowired
    private TicketKategorieRepository ticketKategorieRepository;

    public List<TicketKategorie>  findAll()
    {
        List<TicketKategorie> list = ticketKategorieRepository.findAll();
        if(list.isEmpty()) {
            throw ExeException.notFound("TicketKategorie");
        }
        return list;
    }

    public TicketKategorie findById(Long id)throws ExeException
    {
        if(id == null){ throw ExeException.idNullOrBad("TicketKategorie");}
        TicketKategorie ticketKategorie = ticketKategorieRepository.findById(id)
                .orElseThrow(()-> ExeException.notFound("TicketKategorie", id)  );
        return ticketKategorie;
    }

    public void delete(Long id) throws ExeException
    {
        TicketKategorie tiketKategorie = this.findById(id);
        ticketKategorieRepository.deleteById(id);
    }

    public TicketKategorie save(@Valid TicketKategorieRequest request)
    {
        try{
            TicketKategorie ticketKategorie = new TicketKategorie(
                    request.name(),
                    request.description()
            );
            ticketKategorieRepository.save(ticketKategorie);
            return ticketKategorie;
        }catch(ExeException e){
            throw e;
        }catch (Throwable e){
            throw ExeException.ofInternalError(e.getMessage());
        }
    }

    public TicketKategorie save(TicketKategorie ticketKategorie) throws ExeException
    {
        if(ticketKategorie == null){ throw ExeException.ofInternalError("Ticket Kategorie"); }
        try {
            ticketKategorieRepository.save(ticketKategorie);
            return  ticketKategorie;
        }catch(ExeException e){
            throw e;
        }catch (Throwable e){
            throw new ExeException(e.getMessage());
        }
    }

    @Transactional
    public TicketKategorie update(@NotNull Long id, @Valid TicketKategorieRequest request) throws ExeException {

        if (id == null) {
            throw ExeException.idNullOrBad("TicketKategorie");
        }

        TicketKategorie existing = ticketKategorieRepository.findById(id)
                .orElseThrow(() -> ExeException.notFound("TicketKategorie", id));

        // aggiorna campi modificabili
        existing.setName(request.name());
        existing.setDescription(request.description());
        existing.setId(id);

        return ticketKategorieRepository.save(existing);
    }
}//end  class
