package at.spengergasse.dp_backend.repository.event;

import at.spengergasse.dp_backend.models.event.Elements;
import at.spengergasse.dp_backend.models.event.ElementsId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ElementsRepository extends JpaRepository<Elements, ElementsId> {
    List<Elements> findBySeatPlan_Id(Long seatPlanId);
    void deleteBySeatPlan_Id(Long seatPlanId);

    long deleteById_SeatPlanId(Long seatPlanId);


    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE elements e
        SET is_available = false
        WHERE e.type = 'SEAT'
          AND NOT EXISTS (
            SELECT 1
            FROM tickets t
            WHERE t.elements_seat_plan_id = e.seat_plan_id
              AND t.elements_x = e."x"
              AND t.elements_y = e."y"
          )
        """, nativeQuery = true)
    int markFreeSeatsAsUnavailable();
}
