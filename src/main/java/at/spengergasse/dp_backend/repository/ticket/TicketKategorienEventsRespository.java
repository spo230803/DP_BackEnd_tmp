package at.spengergasse.dp_backend.repository.ticket;

import at.spengergasse.dp_backend.models.ticket.TicketKategorienEvents;
import at.spengergasse.dp_backend.models.ticket.TicketKategorienEventsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketKategorienEventsRespository
    extends JpaRepository<TicketKategorienEvents , TicketKategorienEventsId>
{
    List<TicketKategorienEvents> findByEvents_Id(Long eventId);
    List<TicketKategorienEvents> findByTicketKategorie_Id(Long ticketKategorieId);
    Optional< TicketKategorienEvents> findByEvents_IdAndTicketKategorie_Id(Long eventId, Long ticketKategorieId );


}
