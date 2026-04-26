package at.spengergasse.dp_backend.dto.Seatplan;
import at.spengergasse.dp_backend.models.enums.ElementType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


public record Element(

        @NotBlank
        Long seatPlanId,

        @NotNull
        Integer x,

        @NotNull
        Integer y,

        @NotNull
        ElementType type,

        String label,

        boolean available,

        String row,

        Integer number
) {}