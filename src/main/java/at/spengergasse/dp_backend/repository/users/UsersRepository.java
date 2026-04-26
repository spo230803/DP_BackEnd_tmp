package at.spengergasse.dp_backend.repository.users;

import at.spengergasse.dp_backend.models.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Object getUsersByDiscordId(Long discordId);

    Optional<Users> findByDiscordId(Long discordId);
}
