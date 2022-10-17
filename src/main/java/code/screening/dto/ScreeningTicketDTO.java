package code.screening.dto;

import java.time.LocalDateTime;

public record ScreeningTicketDTO(LocalDateTime screeningDate, Integer screeningFreeSeats) {
}
