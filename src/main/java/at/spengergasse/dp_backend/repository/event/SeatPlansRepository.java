package at.spengergasse.dp_backend.repository.event;

import at.spengergasse.dp_backend.models.event.SeatPlans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatPlansRepository extends JpaRepository<SeatPlans, Long> {
	List<SeatPlans>  findAllByEventIs(Long id);
	Optional<SeatPlans> findByEventId(Long eventId);
	boolean existsByEventId(Long eventId);
}
