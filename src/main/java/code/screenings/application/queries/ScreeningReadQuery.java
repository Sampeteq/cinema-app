package code.screenings.application.queries;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScreeningReadQuery(Long filmId, LocalDateTime date) {}
