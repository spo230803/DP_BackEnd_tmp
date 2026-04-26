package at.spengergasse.dp_backend.mapper;

import at.spengergasse.dp_backend.dto.discor.DiscordUserRequest;
import at.spengergasse.dp_backend.models.users.Users;

public final class UserMapper
{
    private UserMapper() {}

    public static DiscordUserRequest toRequest(Users user)
    {
        return new DiscordUserRequest(
            user.getId(),
            user.getName(),
            user.getEmail(),
            null
        );
    }
}
