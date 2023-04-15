package code.films.domain.queries;

import lombok.Builder;

import java.util.UUID;

@Builder
public record SearchScreeningSeatsQuery(UUID screeningId) {
}
