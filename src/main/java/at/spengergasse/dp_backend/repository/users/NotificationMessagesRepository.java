package at.spengergasse.dp_backend.repository.users;

import at.spengergasse.dp_backend.models.users.NotificationMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationMessagesRepository extends JpaRepository<NotificationMessages, Integer> {
}
