package at.spengergasse.dp_backend.models.ticket;

import at.spengergasse.dp_backend.models.BaseEntity;
import at.spengergasse.dp_backend.models.event.Elements;
import at.spengergasse.dp_backend.models.event.Events;

import at.spengergasse.dp_backend.models.users.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
@Getter
public class Tickets extends BaseEntity
{

    @NotBlank
    @Size(min =1, max = 50)
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 8, fraction = 2) // numeric(10,2) => fino a 8 cifre intere + 2 decimali
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id")
    private Events event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private Discounts discount;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_kategorie_id")
    private TicketKategorie  ticketKategorie;


    /**
     * FK composta verso elements(seat_plan_id, x, y)
     * colonne in tickets: elements_seat_plan_id, elements_row, elements_number
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "elements_seat_plan_id", referencedColumnName = "seat_plan_id", nullable = false),
            @JoinColumn(name = "elements_x", referencedColumnName = "x", nullable = false),
            @JoinColumn(name = "elements_y", referencedColumnName = "y", nullable = false)
    })
    private Elements element;

    //JPA
    protected Tickets() {}

    // Business
    public Tickets(
            @NotBlank String status,
            @NotNull Users user,
            @NotNull Events event,
            Discounts discount, // nullable
            @NotNull TicketKategorie ticketKategorie,
            @NotNull BigDecimal price,
            @NotNull Elements element
    ) {
        if (status == null || status.isBlank()) throw new IllegalArgumentException("status is blank");
        if (user == null) throw new IllegalArgumentException("user is null");
        if (event == null) throw new IllegalArgumentException("event is null");
        if (ticketKategorie == null) throw new IllegalArgumentException("ticketKategorie is null");
        if (price == null) throw new IllegalArgumentException("price is null");
        if (element == null) throw new IllegalArgumentException("element is null");

        this.status = status;
        this.user = user;
        this.event = event;
        this.discount = discount;
        this.ticketKategorie = ticketKategorie;
        this.price = price;
        this.element = element;
    }


    public void setUser(@NotNull Users user) { this.user = user; }

    public void setEvent(@NotNull Events event) { this.event = event; }

    public void setDiscount(Discounts discount) { this.discount = discount; }

    public void setTicketKategorie(@NotNull TicketKategorie ticketKategorie) {
        this.ticketKategorie = ticketKategorie;
    }

    public void setPrice(@NotNull BigDecimal price) { this.price = price; }

    public void setElement(@NotNull Elements element) { this.element = element; }

    public void setStatus(@NotBlank String status) {this.status = status;   }
}//end class
