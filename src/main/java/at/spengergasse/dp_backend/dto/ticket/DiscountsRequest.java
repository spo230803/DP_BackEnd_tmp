package at.spengergasse.dp_backend.dto.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscountsRequest
{
    @NotBlank
    @JsonProperty("code")
    private String code;

    @NotNull
    @JsonProperty("percentage")
    private Integer percentage;

    @JsonProperty("conditions")
    private String conditions;
}
