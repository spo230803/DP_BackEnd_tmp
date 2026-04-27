package at.spengergasse.dp_backend.service.users;


import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.users.DiscordLogin;
import at.spengergasse.dp_backend.repository.users.DiscordLoginRepository;
import at.spengergasse.dp_backend.service.BaseService;
import at.spengergasse.dp_backend.system.SessionNameVar;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscordLoginService extends BaseService
{
    @Autowired
    private DiscordLoginRepository discordLoginRepository;

    SessionNameVar sessionNameVar = new SessionNameVar();


    public void saveLogin(DiscordLogin login){
        // Upsert: update login_time if record exists, otherwise insert
        discordLoginRepository.findByUsersId_Id(login.getUsersId().getId())
            .ifPresentOrElse(
                existing -> {
                    existing.setLoginTime(java.time.LocalDateTime.now());
                    discordLoginRepository.save(existing);
                },
                () -> discordLoginRepository.save(login)
            );
    }


    public List<DiscordLogin> findAllByIdUser(HttpSession session) throws ExeException
    {
        if(session==null){
            throw new ExeException("session is null");
        }
        if(session.getAttribute( sessionNameVar.SESSION_IS_ADMIN)==null || ! (boolean) session.getAttribute(sessionNameVar.SESSION_IS_ADMIN)){
            throw new ExeException("session is not admin", HttpStatus.FORBIDDEN ,"session is not admin");
        }
        return discordLoginRepository.findAllByUsersId((Long) session.getAttribute(sessionNameVar.SESSION_ID_USER_UNSERE_DB));
    }

}//end Class