package code.screening.dto;

import java.time.LocalDateTime;

public record ScreeningTicketDataDTO(LocalDateTime screeningDate, Integer screeningMinAge, Integer screeningFreeSeats) {
}
