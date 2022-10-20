package code.screenings.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScreeningReservationData(LocalDateTime screeningDate, int screeningFreeSeats) {
}
