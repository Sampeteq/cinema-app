package code.screenings.client.queries;

import lombok.Builder;

import java.util.UUID;

@Builder
public record GetScreeningSeatsQuery(Long screeningId) {
}
