package at.spengergasse.dp_backend.dto.EmailModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TicketKaufen
{
    private String userEmail;
    private String userName;
    private String eventName;
    private String iban;
    private BigDecimal preis;

}
