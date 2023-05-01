package code.seats.client.queries;

import lombok.Builder;

import java.util.UUID;

@Builder
public record GetScreeningSeatsQuery(UUID screeningId) {
}
