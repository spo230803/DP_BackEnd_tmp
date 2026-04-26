package at.spengergasse.dp_backend.dto.ticket;

import at.spengergasse.dp_backend.models.ticket.Tickets;

import java.math.BigDecimal;

public record TicketsResponse(
        Long id,
        String status,
        BigDecimal price,
        Long userId,
        Long eventId,
        Long ticketKategorieId,
        Long discountId,
        Long elementsSeatPlanId,
        Integer elementsX,
        Integer elementsY
) {
    public static TicketsResponse from(Tickets t) {
        return new TicketsResponse(
                t.getId(),
                t.getStatus(),
                t.getPrice(),
                t.getUser().getId(),
                t.getEvent().getId(),
                t.getTicketKategorie().getId(),
                t.getDiscount() != null ? t.getDiscount().getId() : null,
                t.getElement().getId().getSeatPlanId(),
                t.getElement().getId().getX(),
                t.getElement().getId().getY()
        );
    }
}
