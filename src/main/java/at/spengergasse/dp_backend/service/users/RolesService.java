package at.spengergasse.dp_backend.service.users;

import at.spengergasse.dp_backend.dto.users.RolesRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.users.Roles;
//import at.spengergasse.dp_backend.repository.users.RolesRepository;
import at.spengergasse.dp_backend.service.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;

@Service
public class RolesService extends BaseService
{
    /*
    @Autowired
    private RolesRepository rolesRepository;

    public List<Roles> findAll()throws EntityNotFoundException
    {
        List<Roles> list = rolesRepository.findAll();
        if (list.isEmpty()) {
            throw new EntityNotFoundException("Roles not found");
        }
        return list;
    }

    public Roles findById(Long id) throws EntityNotFoundException
    {
        if(id == null) {
            throw new ExeException("Roles id is bad");
        }
        Optional<Roles> role = rolesRepository.findById(id);
        if(role.isEmpty()) {
            throw new EntityNotFoundException("Roles not found with id " + id);
        }
        return role.get();
    }

    public Optional<Roles> findFirst()
    {
        Optional<Roles> role =  rolesRepository.findFirstBy();
        return role;
    }

    public Optional<Roles> findByIdOptional(Long id) throws ExeException
    {
        if(id == null) {
            throw new ExeException("Roles id is bad");
        }
        Optional<Roles> role = rolesRepository.findById(id);
        return role;
    }

    public void deleteById(Long id)throws EntityNotFoundException
    {
        try{
            this.findById(id);
            rolesRepository.deleteById(id);
        }catch (EntityNotFoundException e) { throw e;}
    }

    public Roles save(@Valid RolesRequest role) throws ExeException
    {
        Roles newRole;
        try{
            newRole = new Roles(
                    role.getName(),
                    role.getDescription()
            );
        }catch (Exception e){
            throw new  ExeException(e.getMessage());
        }

        rolesRepository.save(newRole);
        return  newRole;
    }

    public Roles save(Roles role)  throws ExeException
    {
        try{
            rolesRepository.save(role);
            return  role;
        } catch (Exception e) {
            throw new ExeException(e);
        }
    }

     */
}//end Class

