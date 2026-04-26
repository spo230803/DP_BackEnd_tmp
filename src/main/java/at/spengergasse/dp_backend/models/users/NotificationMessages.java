package at.spengergasse.dp_backend.models.users;


import at.spengergasse.dp_backend.models.BaseEntity;
import at.spengergasse.dp_backend.models.ticket.Tickets;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
@Table(name = "notification_messages")
@Getter
public class NotificationMessages extends BaseEntity
{
    @Size(min = 1 , max = 50)
    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "content", nullable = false)
    private String content;

    @Size(min = 1, max=100)
    @Column(name = "trigger_action", nullable = false, length = 100)
    private String triggerAction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id")
    private Tickets ticket;

    public void setTicket(Tickets ticket) {
        this.ticket = ticket;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    //JPA
    protected NotificationMessages() {}

    //Bussines
    public NotificationMessages(String type, String content, String triggerAction, Users user, Tickets ticket) {
        this.type = type;
        this.content = content;
        this.triggerAction = triggerAction;
        this.user = user;
        this.ticket = ticket;
    }
}//end Class
