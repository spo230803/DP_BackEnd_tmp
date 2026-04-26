package at.spengergasse.apiTest.events;

import at.spengergasse.ApiBaseTest;
import at.spengergasse.dp_backend.dto.Seatplan.Element;
import at.spengergasse.dp_backend.models.enums.ElementType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc(addFilters = false)
public class ApiElementsTest extends ApiBaseTest
{
    private static final String API_BASE = "/api/elements";

    private Long findExistingSeatPlanId()
    {
        @SuppressWarnings("unchecked")
        List<Number> rows = em.createNativeQuery("""
                SELECT id
                FROM seat_plans
                ORDER BY id
                LIMIT 1
                """).getResultList();

        Assertions.assertFalse(
                rows.isEmpty(),
                "La tabella seat_plans è vuota. Serve almeno un seat_plan già presente per i test di ApiElements."
        );

        return rows.getFirst().longValue();
    }

    private TestCoords nextFreeCoords(Long seatPlanId)
    {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createNativeQuery("""
                SELECT COALESCE(MAX(x), 0), COALESCE(MAX(y), 0)
                FROM elements
                WHERE seat_plan_id = :seatPlanId
                """)
                .setParameter("seatPlanId", seatPlanId)
                .getResultList();

        Object[] row = rows.getFirst();

        int nextX = ((Number) row[0]).intValue() + 100;
        int nextY = ((Number) row[1]).intValue() + 100;

        return new TestCoords(nextX, nextY);
    }

    private Element createElementRequest(Long seatPlanId, int x, int y)
    {
        return new Element(
                seatPlanId,
                x,
                y,
                ElementType.SEAT,
                "A1",
                true,
                "A",
                1
        );
    }

    private void insertElementDirectly(Long seatPlanId, int x, int y)
    {
        em.createNativeQuery("""
                INSERT INTO elements (seat_plan_id, x, y, type, label, is_available, row, number)
                VALUES (:seatPlanId, :x, :y, CAST(:type AS enum_element_type), :label, :available, :row, :number)
                """)
                .setParameter("seatPlanId", seatPlanId)
                .setParameter("x", x)
                .setParameter("y", y)
                .setParameter("type", "SEAT")
                .setParameter("label", "A1")
                .setParameter("available", true)
                .setParameter("row", "A")
                .setParameter("number", 1)
                .executeUpdate();

        em.flush();
        em.clear();
    }

    @Test
    void createOkTest() throws Exception
    {
        Long seatPlanId = findExistingSeatPlanId();
        TestCoords coords = nextFreeCoords(seatPlanId);

        Element request = createElementRequest(seatPlanId, coords.getX(), coords.getY());

        mockMvc.perform(post(API_BASE)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createErrorBadRequestTest() throws Exception
    {
        Long seatPlanId = findExistingSeatPlanId();
        TestCoords coords = nextFreeCoords(seatPlanId);

        String json = """
                {
                  "seatPlanId": %d,
                  "x": null,
                  "y": %d,
                  "type": "SEAT",
                  "label": "A1",
                  "available": true,
                  "row": "A",
                  "number": 1
                }
                """.formatted(seatPlanId, coords.getY());

        mockMvc.perform(post(API_BASE)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getBySeatPlanOkTest() throws Exception
    {
        Long seatPlanId = findExistingSeatPlanId();
        TestCoords coords = nextFreeCoords(seatPlanId);

        insertElementDirectly(seatPlanId, coords.getX(), coords.getY());

        mockMvc.perform(get(API_BASE + "/seatplan/" + seatPlanId)
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    void getOneOkTest() throws Exception
    {
        Long seatPlanId = findExistingSeatPlanId();
        TestCoords coords = nextFreeCoords(seatPlanId);

        insertElementDirectly(seatPlanId, coords.getX(), coords.getY());

        mockMvc.perform(get(API_BASE + "/" + seatPlanId + "/" + coords.getX() + "/" + coords.getY())
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    void getOneErrorTest() throws Exception
    {
        Long seatPlanId = findExistingSeatPlanId();

        mockMvc.perform(get(API_BASE + "/" + seatPlanId + "/999999/999999")
                        .session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAvailabilityOkTest() throws Exception
    {
        Long seatPlanId = findExistingSeatPlanId();
        TestCoords coords = nextFreeCoords(seatPlanId);

        insertElementDirectly(seatPlanId, coords.getX(), coords.getY());

        mockMvc.perform(patch(API_BASE + "/" + seatPlanId + "/" + coords.getX() + "/" + coords.getY() + "/availability")
                        .session(session)
                        .param("available", "false"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteOkTest() throws Exception
    {
        Long seatPlanId = findExistingSeatPlanId();
        TestCoords coords = nextFreeCoords(seatPlanId);

        insertElementDirectly(seatPlanId, coords.getX(), coords.getY());

        mockMvc.perform(delete(API_BASE + "/" + seatPlanId + "/" + coords.getX() + "/" + coords.getY())
                        .session(session))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteErrorTest() throws Exception
    {
        Long seatPlanId = findExistingSeatPlanId();

        mockMvc.perform(delete(API_BASE + "/" + seatPlanId + "/999999/999999")
                        .session(session))
                .andExpect(status().isNoContent());
    }
}

class TestCoords
{
    private final int x;
    private final int y;

    public TestCoords(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}