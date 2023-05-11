package code.screenings.client.queries;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record GetScreeningsQuery(Long filmId, LocalDateTime date) {}
