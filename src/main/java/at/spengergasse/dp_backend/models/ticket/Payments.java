package at.spengergasse.dp_backend.models.ticket;

import at.spengergasse.dp_backend.models.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
public class Payments extends BaseEntity
{
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Size(min = 1 , max=50)
    @Column(name = "method", nullable = false, length = 50)
    private String method;

    @Size(min = 1, max = 50)
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id")
    private Tickets ticket;

    public void setTicket(Tickets ticket) {
        this.ticket = ticket;
    }

    //JPA
    protected Payments() {}

    //Bussines
    public Payments(BigDecimal amount, String method, String status, LocalDateTime timestamp, Tickets ticket) {
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.timestamp = timestamp;
        this.ticket = ticket;
    }
}//end Class
