package at.spengergasse.dp_backend.dto.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TicketsRequest(
        @NotBlank
        @JsonProperty("status")
        String status,

        @NotNull
        @Min(1)
        @JsonProperty("user_id")
        Long userId,

        @NotNull
        @Min(1)
        @JsonProperty("event_id")
        Long eventId,

        @JsonProperty("discount_id")
        @Min(1)
        Long discountId,

        @NotNull
        @Min(1)
        @JsonProperty("ticket_kategorie_id")
        Long ticketKategorieId,

        @NotNull
        @JsonProperty("price")
        BigDecimal price,

        // FK composta verso public.elements(seat_plan_id, row, number)
        @NotNull
        @Min(1)
        @JsonProperty("elements_seat_plan_id")
        Long  elementsSeatPlanId,

        @NotNull
        @Min(0)
        @JsonProperty("elements_x")
        Integer elementsX,

        @NotNull
        @Min(0)
        @JsonProperty("elements_y")
        Integer elementsY
) {}