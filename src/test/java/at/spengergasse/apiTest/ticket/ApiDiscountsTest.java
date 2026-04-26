package at.spengergasse.apiTest.ticket;

import at.spengergasse.ApiBaseTest;
import at.spengergasse.MyFactory;
import at.spengergasse.dp_backend.dto.ticket.DiscountsRequest;
import at.spengergasse.dp_backend.models.ticket.Discounts;
import at.spengergasse.dp_backend.service.ticket.DiscountsService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc(addFilters = false)
public class ApiDiscountsTest extends ApiBaseTest
{
    private static final String API_BASE = "/api/Discounts";

    @Autowired
    private DiscountsService discountsService;

    private Discounts createDiscount()
    {
        DiscountsRequest request = new DiscountsRequest();
        request.setCode("TEST_CODE_123");
        request.setPercentage(20);
        request.setConditions("TEST CONDITIONS");

        Discounts discount = discountsService.save(request);

        em.flush();
        em.clear();

        return discount;
    }


    @Test
    void findAllOkTest() throws Exception
    {
        createDiscount();

        mockMvc.perform(get(API_BASE + "/getAll")
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    void findAllErrorTest() throws Exception
    {
        mockMvc.perform(get(API_BASE + "/getAll")
                        .session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdOkTest() throws Exception
    {
        Discounts discount = createDiscount();

        mockMvc.perform(get(API_BASE + "/" + discount.getId())
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdErrorTest() throws Exception
    {
        mockMvc.perform(get(API_BASE + "/-1")
                        .session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllNotInUseOkTest() throws Exception
    {
        createDiscount();

        mockMvc.perform(get(API_BASE + "/getAllNotInUse")
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    void findNotInUseByIdOkTest() throws Exception
    {
        Discounts discount = createDiscount();

        mockMvc.perform(get(API_BASE + "/getNotInUser/" + discount.getId())
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    void createOkTest() throws Exception
    {
        DiscountsRequest request = new DiscountsRequest();
        request.setCode("NEW_CODE_123");
        request.setPercentage(15);
        request.setConditions("NEW CONDITIONS");

        mockMvc.perform(post(API_BASE + "/create")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createErrorBadRequestTest() throws Exception
    {
        String json = """
                {
                  "code": "",
                  "percentage": 20,
                  "conditions": "TEST"
                }
                """;

        mockMvc.perform(post(API_BASE + "/create")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateOkTest() throws Exception
    {
        Discounts discount = createDiscount();

        DiscountsRequest request = new DiscountsRequest();
        request.setCode("UPDATED_CODE");
        request.setPercentage(30);
        request.setConditions("UPDATED");

        mockMvc.perform(put(API_BASE + "/" + discount.getId())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateErrorNotFoundTest() throws Exception
    {
        DiscountsRequest request = new DiscountsRequest();
        request.setCode("UPDATED_CODE");
        request.setPercentage(30);
        request.setConditions("UPDATED");

        mockMvc.perform(put(API_BASE + "/-1")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOkTest() throws Exception
    {
        Discounts discount = createDiscount();

        mockMvc.perform(delete(API_BASE + "/" + discount.getId())
                        .session(session))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteErrorTest() throws Exception
    {
        mockMvc.perform(delete(API_BASE + "/-1")
                        .session(session))
                .andExpect(status().isNotFound());
    }
}
