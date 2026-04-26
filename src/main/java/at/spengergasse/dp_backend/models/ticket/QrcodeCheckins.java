package at.spengergasse.dp_backend.models.ticket;

import at.spengergasse.dp_backend.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "qrcode_checkins")
@Getter
public class QrcodeCheckins extends BaseEntity
{
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "device_info", nullable = true)
    private String deviceInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false,  cascade = CascadeType.ALL)
    @JoinColumn(name = "tickets_id")
    private Tickets tickets;

    public void setTickets(Tickets tickets) {
        this.tickets = tickets;
    }
}//end Class
