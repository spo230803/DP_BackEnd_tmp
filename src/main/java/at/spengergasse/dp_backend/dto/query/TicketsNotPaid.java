package at.spengergasse.dp_backend.dto.query;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record TicketsNotPaid
(
    String eventTitle,
    LocalDateTime eventDate,
    String userName,
    String userEmail,
    BigDecimal price,
    float payment
){}
