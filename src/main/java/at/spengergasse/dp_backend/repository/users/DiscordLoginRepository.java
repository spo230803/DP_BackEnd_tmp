package at.spengergasse.dp_backend.repository.users;

import at.spengergasse.dp_backend.models.users.DiscordLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscordLoginRepository extends JpaRepository<DiscordLogin, Long> {

    List<DiscordLogin> findAllByUsersId(Long idUser);

    Optional<DiscordLogin> findByUsersId_Id(Long userId);
}
