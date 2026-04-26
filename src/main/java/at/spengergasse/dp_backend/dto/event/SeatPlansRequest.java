package at.spengergasse.dp_backend.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatPlansRequest
{
    @NotBlank
    private String name;

    //@JsonProperty("img_path")
    //private String imgPath;

    @NotNull
    @Min(0)
    @JsonProperty("event_id")
    private Long eventId;
}
