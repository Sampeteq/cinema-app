package code.screenings.client.queries;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetScreeningsQuery(Long filmId, LocalDateTime date) {}
