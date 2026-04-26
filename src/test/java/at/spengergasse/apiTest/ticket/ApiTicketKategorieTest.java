package at.spengergasse.apiTest.ticket;

import at.spengergasse.ApiBaseTest;
import at.spengergasse.MyFactory;
import at.spengergasse.dp_backend.service.ticket.TicketKategorieService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc(addFilters = false)
public class ApiTicketKategorieTest extends ApiBaseTest
{
    private final String API_BASE = "/api/TicketKategorie";

    @Autowired
    private TicketKategorieService ticketKategorieService;

    @Test
    public void getAll_tkOK() throws Exception
    {
        ticketKategorieService.save(MyFactory.ticketKategorie());
        em.flush();
        em.clear();

        mockMvc.perform(get(API_BASE).session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdOkayTest() throws Exception
    {
        var x =  MyFactory.ticketKategorie();
        ticketKategorieService.save(x);
        em.flush();
        em.clear();

        mockMvc.perform(get(API_BASE+"/"+x.getId()).session(session))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdErrorTest() throws Exception
    {
         mockMvc.perform(get(API_BASE+"/-1").session(session))
                .andDo(print())
                .andExpect(status().isNotFound());
    }



}// end class
