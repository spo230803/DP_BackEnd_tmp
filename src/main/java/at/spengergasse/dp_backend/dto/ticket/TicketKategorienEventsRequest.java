package at.spengergasse.dp_backend.dto.ticket;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TicketKategorienEventsRequest(
        @NotNull
        Long eventId,

        @NotNull
        Long ticketKategorienId,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal price
)
{}
