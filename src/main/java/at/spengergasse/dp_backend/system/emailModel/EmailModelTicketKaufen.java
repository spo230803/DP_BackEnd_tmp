package at.spengergasse.dp_backend.system.emailModel;

import at.spengergasse.dp_backend.dto.EmailModel.TicketKaufen;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.service.system.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.attoparser.IDocumentHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailModelTicketKaufen extends EmailModelBase
{

    public EmailModelTicketKaufen(EmailService emailService) {
        super(emailService);
    }

    @Override
    protected void sendEmailSimple(Object[] emailDati) throws ExeException
    {
        try {
            System.out.println(emailDati);
            TicketKaufen ticketKaufen = (TicketKaufen) emailDati[0];
            StringBuilder bodyEmail = new StringBuilder();

            bodyEmail.append("Liebe " + ticketKaufen.getUserName() + " , \n\n");
            bodyEmail.append("Dein Ticket ist für das Event " + ticketKaufen.getEventName() + " gebucht. \n");
            bodyEmail.append("Hier ist die Datei für \"Bezahlen mit Ticket\":\n\n");

            bodyEmail.append("\tIBAN : "+ticketKaufen.getIban()+"\n");
            bodyEmail.append("\tPREIS : "+ticketKaufen.getPreis()+" Euro\n\n");

            bodyEmail.append("Liebe Grüße");


            this.emailService.sendEmailSiple(
                    ticketKaufen.getUserEmail(),
                    "Info Bezhalen Event " +ticketKaufen.getUserName(),
                    bodyEmail.toString()
            );

        } catch (Exception e) {
            log.warn(e.getMessage());
            throw ExeException.ofInternalError("EMAIL - "+e.getMessage());
        }
    }

    @Override
    protected void sendEmailHTML(Object[] emailDati) throws ExeException
    {
        try{
            TicketKaufen ticketKaufen = (TicketKaufen) emailDati[0];
            StringBuilder bodyEmail = new StringBuilder();

            bodyEmail.append("Liebe " + ticketKaufen.getUserName() + ", <br>");
            bodyEmail.append("Dein Ticket ist für das Event <b>" + ticketKaufen.getUserName() + "</b> gebucht. <br>");
            bodyEmail.append("Hier ist die Datei für \"Bezahlen mit Ticket\":<br><br>");

            bodyEmail.append("<ul><li>IBAN : "+ticketKaufen.getIban()+"</li>");
            bodyEmail.append("<li>PREIS : "+ticketKaufen.getPreis()+" Euro</li></ul><br><br>");

            bodyEmail.append("<i>Liebe Grüße</i>");


            this.emailService.sendEmailSiple(
                    ticketKaufen.getUserEmail(),
                    "Info Bezhalen Event " +ticketKaufen.getEventName(),
                    bodyEmail.toString()
            );
        } catch (Exception e) {
            throw ExeException.ofBadRequest("EMAIL - "+e.getMessage());
        }
    }
}
