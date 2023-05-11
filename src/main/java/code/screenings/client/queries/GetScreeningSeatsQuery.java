package code.screenings.client.queries;

import lombok.Builder;

@Builder
public record GetScreeningSeatsQuery(Long screeningId) {
}
