package at.spengergasse.dp_backend.system.emailModel;

import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.service.system.EmailService;

import java.time.LocalDateTime;

public class EmailModelCornJob extends EmailModelBase
{
    public EmailModelCornJob(EmailService emailService) {
        super(emailService);
    }

    //emailDati
    // 0 = Name CornJob
    // 1 = Descizione

    @Override
    protected void sendEmailSimple(Object[] emailDati) throws ExeException {
        try {
            StringBuilder bodyEmail = new StringBuilder();

            bodyEmail.append("Sehr geehrter Administrator, \n\n");
            bodyEmail.append("am " + LocalDateTime.now().toString() + " wurde der CornJob <" + emailDati[0] + "> ausgeführt.\n");
            bodyEmail.append("Mit folgenden Informationen:\n");
            bodyEmail.append(emailDati[1]);
            bodyEmail.append("\n\n");
            bodyEmail.append("MfG\n");
            bodyEmail.append("System - " + this.globalConstat.getSysAppName());

            this.emailService.sendEmailSiple(
                    this.globalConstat.getEmailAdmin(),
                    "CronJob -" + emailDati[0],
                    bodyEmail.toString()
            );
        }catch (Exception e){
            throw new  ExeException(e.getMessage(), this);
        }
    }

    @Override
    protected void sendEmailHTML(Object[] emailDati) throws ExeException {

    }
}//end class
