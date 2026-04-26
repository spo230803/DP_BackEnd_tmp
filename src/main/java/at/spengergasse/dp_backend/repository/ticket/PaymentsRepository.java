package at.spengergasse.dp_backend.repository.ticket;


import at.spengergasse.dp_backend.models.ticket.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentsRepository  extends JpaRepository<Payments, Integer> {
    Optional<Payments> findByTicket_Id(Long ticketId);

    Optional<Payments> findById(Long id);
}
