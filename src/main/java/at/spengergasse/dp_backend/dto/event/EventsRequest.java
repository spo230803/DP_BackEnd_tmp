package at.spengergasse.dp_backend.dto.event;

import at.spengergasse.dp_backend.models.enums.EventStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.jar.JarOutputStream;


public record EventsRequest(
        @NotBlank
        @Size(min = 1, max = 200)
        String title,

        String subtitle,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime startsDate,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime endsDate,

        @NotNull
        @JsonProperty("location")
        @Size(min = 1, max = 200)
        String location,

        @JsonProperty("iban")
        String iban,

        @NotNull
        EventStatus status
){}

