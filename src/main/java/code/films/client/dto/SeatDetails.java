package code.films.client.dto;

import lombok.Builder;

@Builder
public record SeatDetails(boolean isAvailable, int timeToScreeningInHour) {
}
