package at.spengergasse.dp_backend.service.ticket;

import at.spengergasse.dp_backend.dto.ticket.DiscountsRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.ticket.Discounts;
import at.spengergasse.dp_backend.models.ticket.Tickets;
import at.spengergasse.dp_backend.repository.ticket.DiscountsRepository;
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
public class DiscountsService extends BaseService
{
    @Autowired
    private DiscountsRepository discountsRepository;

    @Autowired
    private TicketService ticketService;


    public List<Discounts> findAll() throws ExeException
    {
        List<Discounts> discounts = discountsRepository.findAll();
        if(discounts.isEmpty()){
            throw ExeException.notFound("Discounts");
        }
        return discounts;
    }

    public Discounts findById(Long id) throws ExeException
    {
        if(id == null){ throw ExeException.idNullOrBad("Discount");}
        Optional<Discounts> discount = Optional.of(discountsRepository.findById(id)
                .orElseThrow(() -> ExeException.notFound("Discountr", id)));
        return  discount.get();
    }

    public List<Discounts> findAllNotInUse() throws ExeException
    {
        List<Discounts> discounts = discountsRepository.findAllNotInUse();
        if(discounts.isEmpty()){
            throw ExeException.notFound("No discounts found");
        }
        return discounts;
    }

    public Discounts findNotInUserById(Long id) throws EntityNotFoundException, ExeException
    {
        if(id == null){
            throw new ExeException("ID is null");
        }
        Optional<Discounts> discount = discountsRepository.findByIdAndNotInUse(id);
        if(discount.isEmpty()){
            throw new EntityNotFoundException("No discounts found whit ID = "+id);
        }
        return discount.get();
    }



    public void deleteById(Long id) throws  ExeException
    {

        if(id == null){
            throw ExeException.idNullOrBad("discountId is null");
        }

        Optional<Discounts> discounts = Optional.of(discountsRepository.findById(id)
                .orElseThrow(() -> ExeException.notFound("Discontu", id)));
        discountsRepository.deleteById(id);

    }

    public Discounts  save(Discounts discounts)
    {
        return discountsRepository.save(discounts);
    }

    public Discounts save(@Valid DiscountsRequest request) throws ExeException
    {
        if(request == null){throw  ExeException.idNullOrBad("Request cannot be null");}

        Discounts newDiscounts = new Discounts(
            request.getCode(),
            request.getPercentage(),
            request.getConditions()
        );
        return save(newDiscounts);
    }

    @Transactional
    public Discounts update(Long id, @Valid DiscountsRequest request) throws ExeException {

        if (id == null) {
            throw ExeException.idNullOrBad("Discount");
        }
        if (request == null) {
            throw ExeException.idNullOrBad("Request cannot be null");
        }

        Discounts existing = discountsRepository.findById(id)
                .orElseThrow(() -> ExeException.notFound("Discount", id));

        // (Opzionale ma consigliato) Se non vuoi permettere update quando è in uso:
        // boolean inUse = ticketService.existsByDiscountId(id); // devi avere questo metodo
        // if (inUse) throw ExeException.ofConflict("Discount is in use and cannot be updated");

        // aggiorna campi
        existing.setCode(request.getCode());
        existing.setPercentage(request.getPercentage());
        existing.setConditions(request.getConditions());
        existing.setId(id);

        try {
            return discountsRepository.save(existing);
        } catch (Throwable e) {
            throw ExeException.ofInternalError(e.getMessage());
        }
    }
}//end Class
