package at.spengergasse.dp_backend.models.event;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 *  Klasse, die den Primärschlüssel der Tabelle „elements” enthält
 */


@Getter
@Embeddable
public class ElementsId
{
    @Column(name = "seat_plan_id")
    private Long seatPlanId;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Integer y;

    public ElementsId(@NotNull @Min(1) Long seatPlanId,@NotNull Integer x, Integer y) {
        this.seatPlanId = seatPlanId;
        this.x = x;
        this.y = y;
    }

    //JPA
    protected ElementsId() {}
}
