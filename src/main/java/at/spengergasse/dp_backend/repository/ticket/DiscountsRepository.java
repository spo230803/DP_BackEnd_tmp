package at.spengergasse.dp_backend.repository.ticket;

import at.spengergasse.dp_backend.models.ticket.Discounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountsRepository extends JpaRepository<Discounts, Long>
{
    @Query(value = """
        SELECT d.*
        FROM discounts d
        WHERE NOT EXISTS (
            SELECT 1
            FROM tickets t
            WHERE t.discount_id = d.id
        )
    """, nativeQuery = true)
    List<Discounts> findAllNotInUse();

    @Query(value = """
        SELECT d.*
        FROM discounts d
        WHERE d.id = :discountId
          AND NOT EXISTS (
              SELECT 1
              FROM tickets t
              WHERE t.discount_id = d.id
          )
    """, nativeQuery = true)
    Optional<Discounts> findByIdAndNotInUse(@Param("discountId") Long discountId);

}//end class
