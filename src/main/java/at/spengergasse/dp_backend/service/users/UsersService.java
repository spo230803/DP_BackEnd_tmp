package at.spengergasse.dp_backend.service.users;

import at.spengergasse.dp_backend.GlobalConstat;
import at.spengergasse.dp_backend.dto.discor.DiscordUserRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.users.DiscordLogin;
import at.spengergasse.dp_backend.models.users.Roles;
import at.spengergasse.dp_backend.models.users.Users;
import at.spengergasse.dp_backend.repository.users.DiscordLoginRepository;
//import at.spengergasse.dp_backend.repository.users.RolesRepository;
import at.spengergasse.dp_backend.repository.users.UsersRepository;
import at.spengergasse.dp_backend.service.BaseService;
import at.spengergasse.dp_backend.system.Sessions;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService extends BaseService
{
    @Autowired
    private UsersRepository usersRepository;

    //@Autowired
    //private RolesService rolesService;

    @Autowired
    private DiscordLoginService discordLoginService;


    //Construttor
    public UsersService() {
        this.globalConstat = new GlobalConstat();
    }

    /**
     * Funzione usata per Registare un Nuovo Utente / Utente Loggato
     * @param me (von Discord)
     * @throws ExeException
     */
    public Long userLogIn(DiscordUserRequest me) throws ExeException
    {
        Users userLogin;
        Roles roleLogin;
        try {
            Optional<Users> userInDb = usersRepository.findByDiscordId(me.id());

            if(!userInDb.isPresent()){
                Users userNew = new Users(
                    me.id(),
                    me.username(),
                    me.email()
                );
                usersRepository.save(userNew);
                userLogin = userNew;
            }else {
                userLogin =  userInDb.get();
            }//end if - isset

            discordLoginService.saveLogin(new DiscordLogin(userLogin));
            return userLogin.getId();
        }catch (RuntimeException e){
            throw new ExeException(e.getMessage());
        }
    }//end

    public Users findById(Long id) throws ExeException
    {
        if(id == null) {
            throw new ExeException("User ID is null");
        }
        return usersRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User not found with id " + id));
    }

    public Optional<Users> findByDiscordId(Long discordId) {
        return usersRepository.findByDiscordId(discordId);
    }

    public Users save(Users user)
    {
        if(user == null){
            throw ExeException.idNullOrBad("user");
        }
        return usersRepository.save(user);
    }
}//end Class
