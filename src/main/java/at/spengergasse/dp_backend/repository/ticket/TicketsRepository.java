package at.spengergasse.dp_backend.repository.ticket;

import at.spengergasse.dp_backend.dto.query.TicketsNotPaid;
import at.spengergasse.dp_backend.dto.query.TicketsParticipants;
import at.spengergasse.dp_backend.models.ticket.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketsRepository extends JpaRepository<Tickets, Long> {
    List<Tickets> findByDiscount_Id(Long discountId);


    @Query(value = """
        select
            e.title as eventTitle,
            e."date" as eventDate,
            u.email as userName,
            u."name" as userEmail,
            tk.price as price,
            tk.price as payment
        from tickets t
            inner join users u on t.user_id  = u.id
            inner join events e on t.event_id  = e.id
            inner join ticket_kategorien tk  on t.ticket_kategorie_id  = tk.id
        where not exists  (
            select id
            from payments p
        )
        and e."date" < (NOW() - INTERVAL '1 week')
            union
        select
            e.title as eventTitle,
            e."date" as eventDate,
            u.email as userName,
            u."name" as userEmail,
            tk.price as price,
            (tk.price  - p.amount ) as payment
        from tickets t
            inner join users u on t.user_id  = u.id
            inner join events e on t.event_id  = e.id
            inner join ticket_kategorien tk  on t.ticket_kategorie_id  = tk.id
            inner join payments p on t.id  = p.ticket_id\s
        where e."date" < (NOW() - INTERVAL '1 week')
        and tk.price  > p.amount
        """, nativeQuery = true)
    List<TicketsNotPaid> findTiketNotPaid();

    @Query(value = """
          select
            t.id as ticketsId,
            u."name" as userName,
            u.email as userEmail,
            e."label" as label,
            t.price as price,
            t.status as ticketStatus
        from tickets t
        inner join users u
            on u.id = t.user_id
                    inner join elements e
            on e.seat_plan_id = t.elements_seat_plan_id
           and e.x = t.elements_x
           and e.y = t.elements_y
          where t.event_id = :eventId
        """, nativeQuery = true)
    List<TicketsParticipants> findTicketUserSeatByEventId(Long eventId);
}//end class
