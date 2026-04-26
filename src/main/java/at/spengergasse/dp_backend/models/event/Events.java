package at.spengergasse.dp_backend.models.event;

import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.BaseEntity;
import at.spengergasse.dp_backend.models.enums.EventStatus;
import at.spengergasse.dp_backend.validation.StringGuard;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
public class Events extends BaseEntity
{

    @Size(min = 1, max = 200)
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Size( max = 200)
    @Column(name = "subtitle", nullable = true)
    private  String subtitle;

    @Column(name = "starts_date", nullable = false)
    private LocalDateTime startsDate;

    @Column(name = "ends_date", nullable = false)
    private LocalDateTime endsDate;

    @Size(min = 1, max = 200)
    @Column(name = "location" , nullable = false, length = 200)
    private String location;

    @Size(max = 34)
    @Column(name = "iban", nullable = true, length = 34)
    private String iban;

	@NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    @ColumnTransformer(write = "?::enum_event_status")
    private EventStatus status;

    //JPA
    protected  Events(){}

    public Events(
            String title,
            String subtitle,
            LocalDateTime startsDate,
            LocalDateTime endsDate,
            String location,
            String iban,
            EventStatus eventStatus
    ) throws ExeException
    {
        try{
            StringGuard.validateStringGuardNotBlack(title, "title kann nicht leer sein");
            StringGuard.validateStringGuardNotBlack(location, "location kann nicht leer sein");
        }catch(ExeException e){throw e;}

        this.title = title;
        this.subtitle = subtitle;
        this.startsDate = startsDate;
        this.endsDate = endsDate;
        this.location = location;
        this.iban = iban;
        this.status = eventStatus;
    }

    public void setStatus(EventStatus status){
        this.status = status;
    }
}//end claas
