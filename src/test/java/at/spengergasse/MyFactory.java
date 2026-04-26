package at.spengergasse;

import at.spengergasse.dp_backend.models.enums.ElementType;
import at.spengergasse.dp_backend.models.enums.EventStatus;
import at.spengergasse.dp_backend.models.event.Elements;
import at.spengergasse.dp_backend.models.event.Events;
import at.spengergasse.dp_backend.models.event.SeatPlans;
import at.spengergasse.dp_backend.models.ticket.Discounts;
import at.spengergasse.dp_backend.models.ticket.TicketKategorie;
import at.spengergasse.dp_backend.models.users.Users;

import java.time.LocalDateTime;

public class MyFactory
{
    public static Users user()
    {
        return new Users(
                1234L,
                "User_Test",
                "test@email.com"
        );
    }

    public static Events event()
    {
        return new Events(
            "Title Test",
                "SubTitle Test",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Location Test",
                "AT-01234",
                EventStatus.OFFEN
        );

    }

    public static SeatPlans seatPlan()
    {
        Events event = event();
        event.setId(1L);
        return new SeatPlans(
                "Name Plan",
                event
        );
    }

    public static TicketKategorie ticketKategorie()
    {
        return new TicketKategorie(
                "TicketKategori Name",
                "Test Desciption"
        );
    }

    public static Elements element()
    {
        return new Elements(
                seatPlan(),
                ElementType.SEAT,
                "Label Test",
                1,
                2,
                "Row Test",
                3
        );
    }

    public static Discounts discount()
    {
        return new Discounts(
            "XZYA-432-TEST",
                3,
                "TEST"
        );
    }
}//end Class
