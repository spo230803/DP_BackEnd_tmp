package at.spengergasse.dp_backend.system.emailModel;

import at.spengergasse.dp_backend.GlobalConstat;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.service.system.EmailService;
import org.springframework.stereotype.Service;

@Service
public abstract class EmailModelBase
{

    protected EmailService emailService;

    protected GlobalConstat globalConstat;

    public EmailModelBase(EmailService emailService) {
        this.emailService = emailService;
        globalConstat = new GlobalConstat();
    }


    /**
     * Funzone usata per inviare Email (Un oggetto Figlio per ogni Tipo di Email)
     * Ha anche il compito di scegliare come inviare le Email (Formato Text o HTML)
     * @param emailDati = Datei in Body von Eiam
     * @throws ExeException
     */
    public void sendEmail(Object emailDati[]) throws ExeException {
        if(globalConstat.getEmaiIslHTML()){
            this.sendEmailHTML(emailDati);
        }else {
            this.sendEmailSimple(emailDati);
        }
    }

    //Email in Fomrat Text
    protected abstract void sendEmailSimple(Object emailDati[]) throws ExeException;

    //Email in Format HTML
    protected abstract void sendEmailHTML(Object emailDati[]) throws ExeException;

}//end class
