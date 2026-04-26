package at.spengergasse.apiTest.events;

import at.spengergasse.MyFactory;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.models.event.SeatPlans;
import at.spengergasse.dp_backend.service.event.EventsService;
import at.spengergasse.dp_backend.service.event.SeatPlansService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc(addFilters = false)
public class ApiSeatPlanTest extends ApiEventsTest
{

    private final String API_BASE = "/api/Events/";

    @Autowired
    private SeatPlansService seatPlansService;

    @Autowired
    EventsService eventsService;

    @Test
    void createSeatPlanOkay() throws Exception
    {
        Events evento = MyFactory.event();
        evento =  eventsService.save(evento);

        String json = """
        {
          "name": "Test",
          "event_id": "%s"
        }
        """.formatted(evento.getId());

        mockMvc.perform(post(API_BASE+evento.getId()+"/SeatPlans")
                        .session(session)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createSeatPlanError() throws Exception
    {
        Events evento = MyFactory.event();
        evento =  eventsService.save(evento);

        String json = """
        {
          "name": "",
          "event_id": "%s"
        }
        """.formatted(evento.getId());

        mockMvc.perform(post(API_BASE+evento.getId()+"/SeatPlans")
                        .session(session)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAllOkay() throws Exception {
        Events evento = MyFactory.event();
        SeatPlans seatPlans = MyFactory.seatPlan();
        evento =  eventsService.save(evento);
        eventsService.save(evento);
        seatPlans.setEvent(evento);
        seatPlansService.save(seatPlans);

        mockMvc.perform(get("/api/Events/"+evento.getId()+"/SeatPlans").session(session))
                .andExpect(status().isOk());

    }




    @Test
    void findbyIdErrorTest() throws Exception
    {
        mockMvc.perform(get(API_BASE+"/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletertErrorTest() throws Exception
    {
        Events evento = MyFactory.event();
        SeatPlans seatPlans = MyFactory.seatPlan();
        evento =  eventsService.save(evento);
        eventsService.save(evento);
        seatPlans.setEvent(evento);
        seatPlans =  seatPlansService.save(seatPlans);

        mockMvc.perform(delete(API_BASE+"/"+seatPlans.getId().toString()).session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateErrorTest() throws Exception {
        Events evento = MyFactory.event();
        SeatPlans seatPlans = MyFactory.seatPlan();
        evento =  eventsService.save(evento);
        eventsService.save(evento);
        seatPlans.setEvent(evento);
        seatPlans = seatPlansService.save(seatPlans);

        String json = """
        {
          "name": "New Test",
          "event_id": "%s"
        }
        """.formatted(evento.getId());

        mockMvc.perform(put(API_BASE+"/"+seatPlans.getId()).session(session)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());


    }

}//end Class
