package at.spengergasse.dp_backend.models.event;

import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.BaseEntity;
import at.spengergasse.dp_backend.models.enums.ElementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;


@Entity
@Table(name = "elements")
@Getter
public class Elements {

    @EmbeddedId
    private ElementsId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("seatPlanId")
    @JoinColumn(name = "seat_plan_id", nullable = false)
    private SeatPlans seatPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "enum_element_type")
    @ColumnTransformer(write = "?::enum_element_type")
    private ElementType type;

    @Column(name = "label", length = 100)
    private String label;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;

    @Column(name = "row", nullable = true)
    private String row;

    @Column(name="number", nullable = true)
    private Integer number;

    protected Elements() {}

    public Elements(SeatPlans seatPlan,
                    ElementType type,
                    String label,
                    int x,
                    int y,
                    String row,
                    Integer number) throws ExeException {

        if (seatPlan == null) throw new ExeException("seatPlan null");

        this.seatPlan = seatPlan;
        this.type = type;
        this.label = label;
        this.id = new ElementsId(seatPlan.getId(), x, y);
        this.row = row;
        this.number = number;
    }

    public void setLabel(String newLabel) {
        this.label = newLabel;
    }

    public void setAvailable(boolean available) { this.isAvailable = available;    }

    public void setRow(String row){this.row = row;}

    public void setNumber(Integer number){this.number = number;}

}