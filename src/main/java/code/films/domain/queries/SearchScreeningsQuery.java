package code.films.domain.queries;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record SearchScreeningsQuery (UUID filmId, LocalDateTime date) {}
