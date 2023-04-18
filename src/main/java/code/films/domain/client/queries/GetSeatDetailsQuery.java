package code.films.domain.client.queries;

import lombok.Builder;

import java.util.UUID;

@Builder
public record GetSeatDetailsQuery(UUID seatid) {
}
