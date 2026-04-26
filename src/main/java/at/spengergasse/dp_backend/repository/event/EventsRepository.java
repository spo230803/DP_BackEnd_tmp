package at.spengergasse.dp_backend.repository.event;

import at.spengergasse.dp_backend.models.event.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends JpaRepository<Events, Long> {
}
