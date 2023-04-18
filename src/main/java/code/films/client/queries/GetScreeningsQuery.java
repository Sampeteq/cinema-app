package code.films.client.queries;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record GetScreeningsQuery(UUID filmId, LocalDateTime date) {}
