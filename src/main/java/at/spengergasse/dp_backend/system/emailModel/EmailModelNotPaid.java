package at.spengergasse.dp_backend.system.emailModel;

import at.spengergasse.dp_backend.dto.query.TicketsNotPaid;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.service.system.EmailService;

public class EmailModelNotPaid extends EmailModelBase
{
    public EmailModelNotPaid(EmailService emailService) {
        super(emailService);
    }

    //emailDati
    //0 = ticketsNotPaid

    @Override
    protected void sendEmailSimple(Object[] emailDati) throws ExeException
    {
        try{
            TicketsNotPaid ticketsNotPaid= (TicketsNotPaid) emailDati[0];
            StringBuilder bodyEmail = new StringBuilder();

            bodyEmail.append("Sehr geehrter "+ ticketsNotPaid.userName()+" , \n\n");
            bodyEmail.append("wir möchten Sie daran erinnern, das Ticket für die Veranstaltung ");
            bodyEmail.append(ticketsNotPaid.eventTitle()+"\t");
            bodyEmail.append("am "+ticketsNotPaid.eventDate()+"\n\n");
            bodyEmail.append("Mit Tiket Preis : "+ticketsNotPaid.price()+" €\n");
            bodyEmail.append("Noch offen : "+ticketsNotPaid.payment()+" €\n\n");
            bodyEmail.append("Mit freundlichen Grüßen \n");
            bodyEmail.append(this.globalConstat.getSysAppName());

            this.emailService.sendEmailSiple(
                    ticketsNotPaid.userEmail(),
                    "Erinnern Bezhalen event "+ticketsNotPaid.eventTitle(),
                    bodyEmail.toString()
            );
        }catch (ExeException ex){
            System.out.println(ex.getMessage());
        }
    }//sendEmailSimple

    @Override
    protected void sendEmailHTML(Object[] emailDati) throws ExeException
    {
        TicketsNotPaid ticketsNotPaid= (TicketsNotPaid) emailDati[0];
        StringBuilder bodyEmail = new StringBuilder();

        try {
            bodyEmail.append("Sehr geehrter " + ticketsNotPaid.userName() + " , <br><br>");
            bodyEmail.append("wir möchten Sie daran erinnern, das Ticket für die Veranstaltung ");

            bodyEmail.append("<table>");
                bodyEmail.append("<tr><th>Event</th><td>" + ticketsNotPaid.eventTitle() + "</td></tr>");
                bodyEmail.append("<tr><th>Am</th><td>" + ticketsNotPaid.eventDate() + "</td></tr>");
                bodyEmail.append("<tr><th>Preis Ticket</th><td>" + ticketsNotPaid.price() + "</td></tr>");
                bodyEmail.append("<tr><th>Noch Offen</th><td>" + ticketsNotPaid.payment() + "</td></tr>");
            bodyEmail.append("</table><br><br>");

            bodyEmail.append("Mit freundlichen Grüßen <hr>");
            bodyEmail.append("<i>" + this.globalConstat.getSysAppName() + "</i>");

            this.emailService.sendEmailHtml(
                    ticketsNotPaid.userEmail(),
                    "Erinnern Bezhalen event "+ticketsNotPaid.eventTitle(),
                    bodyEmail.toString()
            );
        }catch (ExeException ex){
            System.out.println(ex.getMessage());
        }catch (Throwable t){
            System.out.println(t.getMessage());
        }
    }//sendEmailHTML


}//end class
