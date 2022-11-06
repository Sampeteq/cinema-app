package code.screenings.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScreeningBookingData(LocalDateTime screeningDate, int screeningFreeSeats) {
}
