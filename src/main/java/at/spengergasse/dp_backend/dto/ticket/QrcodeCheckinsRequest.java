package at.spengergasse.dp_backend.dto.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QrcodeCheckinsRequest
{
    @NotNull
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("device_info")
    private String deviceInfo ;

    @NotNull
    @Min(0)
    @JsonProperty("ticket_id")
    private Long ticketId;
}
