package at.spengergasse.dp_backend.models.event;

import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Entity
@Table(
        name = "seat_plans",
        uniqueConstraints = @UniqueConstraint(name = "uk_seat_plans_event_id", columnNames = "event_id")
)
@Getter
public class SeatPlans extends BaseEntity {

    @Size(min = 1, max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Lob
    @Column(name = "elements_json", nullable = false, columnDefinition = "TEXT")
    private String elementsJson = "[]";

    // 1:1 -> event_id deve essere UNIQUE
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false, unique = true)
    private Events event;

    // JPA
    protected SeatPlans() {}

    // Business
    public SeatPlans(String name, Events event) throws ExeException {
        if (event == null) throw new ExeException("Event is null");
        setName(name);
        this.event = event;
        this.elementsJson = "[]";
    }

    public void setName(@NotBlank String name) throws ExeException {
        if (name == null || name.isBlank()) throw new ExeException("SeatPlan name is blank");
        if (name.length() > 100) throw new ExeException("SeatPlan name too long (max 100)");
        this.name = name.trim();
    }

    public void setEvent(Events event) throws ExeException {
        if (event == null) throw new ExeException("Event is null");
        this.event = event;
    }

    public void setElementsJson(String elementsJson) throws ExeException {
        if (elementsJson == null) throw new ExeException("elementsJson cannot be null");
        this.elementsJson = elementsJson;
    }
}//end Class