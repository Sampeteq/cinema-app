package code.screenings.application.queries;

import lombok.Builder;

@Builder
public record GetScreeningSeatsQuery(Long screeningId) {
}
