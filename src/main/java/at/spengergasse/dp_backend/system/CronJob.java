package at.spengergasse.dp_backend.system;

import at.spengergasse.dp_backend.dto.Seatplan.Element;
import at.spengergasse.dp_backend.dto.query.TicketsNotPaid;
import at.spengergasse.dp_backend.models.event.Elements;
import at.spengergasse.dp_backend.service.event.ElementsService;
import at.spengergasse.dp_backend.service.system.EmailService;
import at.spengergasse.dp_backend.service.ticket.TicketService;
import at.spengergasse.dp_backend.system.emailModel.EmailModelCornJob;
import at.spengergasse.dp_backend.system.emailModel.EmailModelNotPaid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.UnboundElementsSourceFilter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class CronJob
{

    @Autowired
    private TicketService ticketService;

    @Autowired
    protected EmailService emailService;

    @Autowired
    protected ElementsService elementsService;



    /**
     *  Funzione per trovare chi non ha Pagato
     */
    //@Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 0 2 * * *") // Evry Dan at 2:00 AM
    public void userNotPaid()
    {
        try {
            log.info("START: ConJob - userNotPaid() -" + LocalDateTime.now());
            EmailModelCornJob emailModelCornJob = new EmailModelCornJob(emailService);
            EmailModelNotPaid emailModelNotPaid = new EmailModelNotPaid(emailService);
            String descripton = "";


            List<TicketsNotPaid> listNotPaid = ticketService.findTiketNotPaid();

            if (!listNotPaid.isEmpty()) {
                for (TicketsNotPaid ticketsNotPaid : listNotPaid) {
                    emailModelNotPaid.sendEmail(new Object[]{ ticketsNotPaid });
                }
                descripton =  "Unbezahlte Ticket "+listNotPaid.size() +" wurden gefunden";
            } else {
                descripton = "Alle Tickets sind bezahlt";
            }
            log.info("END: ConJob - userNotPaid() -" + LocalDateTime.now());

            emailModelCornJob.sendEmail(new String[]{"user Not paid", descripton});
        }catch (Exception ex){
            log.error("!! ERROR !! : ConJob - userNotPaid() : "+ex.getMessage() );
        }
    }

    @Scheduled(cron = "0 0 * * * *") //Every Hour
    public void freeSeat()
    {
        try{
            log.info("START: ConJob - freeSeat() -  "+ LocalDateTime.now());

            var update = elementsService.freeSeatCronJob();
            log.debug("Update "+update);

            log.info("END: ConJob - freeSeat() -" + LocalDateTime.now());
        }catch (Exception e){
            log.error("!! ERROR !! : ConJob - freeSeat() : "+e.getMessage() );
        }
    }
}//end class
