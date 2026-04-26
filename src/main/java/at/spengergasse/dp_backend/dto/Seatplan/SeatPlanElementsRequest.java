package at.spengergasse.dp_backend.dto.Seatplan;
import lombok.Getter;
import java.util.List;

@Getter
public class SeatPlanElementsRequest {
    private List<Element> elements;
}
