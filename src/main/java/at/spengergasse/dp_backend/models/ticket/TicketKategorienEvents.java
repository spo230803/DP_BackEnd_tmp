package at.spengergasse.dp_backend.models.ticket;

import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.BaseEntity;
import at.spengergasse.dp_backend.models.event.Events;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "ticket_kategorien_events")
@Getter
public class TicketKategorienEvents //extends BaseEntity
{

    @EmbeddedId
    private TicketKategorienEventsId id;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @MapsId("eventId")
    @JoinColumn(name = "events_id" , nullable = false)
    private Events events;


    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @MapsId("ticketKategorieId")
    @JoinColumn(name = "ticket_kategorien_id", nullable = false)
    private TicketKategorie ticketKategorie;

    @Min(0)
    @DecimalMin("0.00")
    @NotNull
    @Column(name="price")
    private BigDecimal price;


    //JPA
    protected TicketKategorienEvents(){}

    //Bussine
    public TicketKategorienEvents(Events event, TicketKategorie ticketKategorie, BigDecimal price) throws ExeException{
        if(event == null )
            throw new ExeException("Event ist null", this);
        if(ticketKategorie == null)
            throw new ExeException("ticketKategorie is null", this);
        if (price == null)
            throw new ExeException("Priche ist nicht enstellen");

        this.events = event;
        this.ticketKategorie = ticketKategorie;
        this.price = price;
        this.id = new TicketKategorienEventsId(event.getId(), ticketKategorie.getId());
    }


    public void setId(@NotNull TicketKategorienEventsId id){ this.id = id;}
    public void setPrice(BigDecimal price){this.price = price;}
}//End Class

