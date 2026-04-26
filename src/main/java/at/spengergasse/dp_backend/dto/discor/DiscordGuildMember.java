package at.spengergasse.dp_backend.dto.discor;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class DiscordGuildMember {
    private List<String> roles;
}
