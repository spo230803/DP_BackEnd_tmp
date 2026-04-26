package at.spengergasse.dp_backend;

import at.spengergasse.dp_backend.system.Autor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GlobalConstat
{
    //System Var
    private String sysVersion = "v1.1";

    private final String sysLatupdate = "2026-03-14";
    private final String sysAppName = "DP - AF-Ticketconnect";
    private final List<Autor> sysAutor = new ArrayList<>();

    //Email
    private final String emailSend = "dp@vbgio.net";                     //Mittente Muss so Sein!
    private final String emailAdmin = "SPO230803@spengergasse.at";      //Admin Email

    @Value("${app.mailHtml:false}")
    private Boolean emaiIslHTML;                        //Flase = Emal Text | True = Email in HTML

    //debug
    @Value("${app.debug:false}")
    private boolean debug;


    public  GlobalConstat()
    {
        sysAutor.add(new Autor("Spoto", "Giorgio Matteo", "Backend - API"));
        sysAutor.add(new Autor("Eldarhanov", "Mirzhan", "Discor API"));
        sysAutor.add(new Autor("Osman","Bassem","Frontend - React"));
        sysAutor.add(new Autor("Poscher","Lukas","Database"));
        sysAutor.add(new Autor("Subotic","Nenad","Proffessor Coordinator"));
    }

    public boolean isDebug(Object obj){
        System.out.println("DEBUG MODE - IN : " +obj.getClass().getName());
        return debug;
    }

}//end GlobalConstat

