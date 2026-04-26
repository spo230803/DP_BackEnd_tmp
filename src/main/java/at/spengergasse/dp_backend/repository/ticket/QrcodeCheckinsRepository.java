package at.spengergasse.dp_backend.repository.ticket;

import at.spengergasse.dp_backend.models.ticket.QrcodeCheckins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QrcodeCheckinsRepository extends JpaRepository<QrcodeCheckins, Integer> {
}
