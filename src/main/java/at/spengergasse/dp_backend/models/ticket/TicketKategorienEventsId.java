package at.spengergasse.dp_backend.models.ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 *  Klasse, die den Primärschlüssel der Tabelle „ticket_kategorien_events” enthält
 */

@Embeddable
@Getter
public class TicketKategorienEventsId implements Serializable
{
    @Column(name = "event")
    private Long eventId;

    @Column(name = "ticket_kategorien_id")
    private Long ticketKategorieId;

    //JPA
    protected TicketKategorienEventsId(){}

    public TicketKategorienEventsId(@NotNull Long eventId, @NotNull Long ticketKategorieId)
    {
        this.eventId = eventId;
        this.ticketKategorieId = ticketKategorieId;
    }
}
