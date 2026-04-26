package at.spengergasse.dp_backend.dto.Seatplan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class SeatPlanLayoutResponse {
    private Long seatPlanId;
    private Integer gridSize;
    private List<Element> elements;
}
