package at.spengergasse.dp_backend.dto.ticket;

import at.spengergasse.dp_backend.models.ticket.Payments;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record PaymentsRequest(

    @NotNull
    @JsonProperty("amount")
    BigDecimal amount,

    @NotBlank
    @JsonProperty("method")
    String method,

    @NotBlank
    @JsonProperty("status")
    String status,

    @NotNull
    @JsonProperty("timestamp")
    LocalDateTime timestamp,


    @NotNull
    @Min(0)
    @JsonProperty("ticket_id")
    Long ticketId
    )
{
    public static PaymentsRequest from(Payments p )
    {
        return new PaymentsRequest(
            p.getAmount(),
            p.getMethod(),
            p.getStatus(),
            p.getTimestamp(),
            p.getTicket().getId()
        );
    }
}
