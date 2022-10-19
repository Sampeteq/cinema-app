package code.reservation.dto;

import java.time.LocalDateTime;

public record ScreeningTicketDTO(LocalDateTime screeningDate, Integer screeningFreeSeats) {
}
