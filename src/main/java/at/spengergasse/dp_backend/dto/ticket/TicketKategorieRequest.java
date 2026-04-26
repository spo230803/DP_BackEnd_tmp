package at.spengergasse.dp_backend.dto.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


public record TicketKategorieRequest (
        @NotBlank
        @JsonProperty("name")
        String name,

        @JsonProperty("description")
         String description
)
{}
