package at.spengergasse.dp_backend.controller;

import at.spengergasse.dp_backend.system.Sessions;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public abstract class BaseController extends Sessions
{

    /**
     * Controllo che sia Loggato
     * @return
     */
    protected boolean isLoggedIn()
    {
        return Boolean.TRUE.equals(session.getAttribute(SESSION_DISCORD_AUTH_KEY));
    }

    /**
     * Controlla se
     * @return
     */
    protected  boolean isAdmin(){return Boolean.TRUE.equals(session.getAttribute(SESSION_IS_ADMIN));}
    protected  boolean isMember(){return Boolean.TRUE.equals(session.getAttribute(SESSION_IS_MEMBER));}


}//end
