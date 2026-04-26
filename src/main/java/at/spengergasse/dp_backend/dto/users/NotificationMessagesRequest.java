package at.spengergasse.dp_backend.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationMessagesRequest
{
    @NotBlank
    @JsonProperty("type")
    private String type;

    @NotBlank
    @JsonProperty("content")
    private String content;

    @NotBlank
    @JsonProperty("trigger_action")
    private String triggerAction;

    @NotNull
    @Min(0)
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("ticket_id")
    private Long ticketId;
}
