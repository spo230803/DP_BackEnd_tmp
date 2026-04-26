package at.spengergasse.dp_backend.dto.discor;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public record DiscordUserRequest(
    @NotNull
    Long id,

    @NotBlank
    String username,

    @NotBlank
    String email,

    @NotBlank
    String avatar
) {
}