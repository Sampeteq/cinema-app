package code.films.client.queries;

import lombok.Builder;

@Builder
public record SeatDetails(boolean isAvailable, int timeToScreeningInHour) {
}
