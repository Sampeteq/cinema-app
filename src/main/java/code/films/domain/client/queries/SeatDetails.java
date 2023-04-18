package code.films.domain.client.queries;

import lombok.Builder;

@Builder
public record SeatDetails(boolean isAvailable, int timeToScreeningInHour) {
}
