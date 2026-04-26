package at.spengergasse.dp_backend.controller.users;

import at.spengergasse.dp_backend.controller.BaseController;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.users.DiscordLogin;
import at.spengergasse.dp_backend.service.users.DiscordLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/DiscordLogin")
public class ApiDiscordLogin extends BaseController
{
    @Autowired
    private DiscordLoginService discordLoginService;


    @GetMapping("/getAllLogin")
    public ResponseEntity<List<DiscordLogin>> getAllLogin()
    {
        List<DiscordLogin> listLogin = discordLoginService.findAllByIdUser(session);
        if(listLogin.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(listLogin, HttpStatus.OK);
    }
}//end class
