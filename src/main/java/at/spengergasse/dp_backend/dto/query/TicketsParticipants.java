package at.spengergasse.dp_backend.dto.query;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TicketsParticipants(
        @NotNull
        Long ticketsId ,

        @NotBlank
        String userName,

        @NotBlank
        String userEmail,

        String label,

        @NotNull
        @Min(1)
        BigDecimal price,

        @NotBlank
        String ticketStatus
) {
}
