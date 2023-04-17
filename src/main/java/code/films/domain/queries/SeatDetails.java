package code.films.domain.queries;

import lombok.Builder;

@Builder
public record SeatDetails(boolean isAvailable, int timeToScreeningInHour) {
}
