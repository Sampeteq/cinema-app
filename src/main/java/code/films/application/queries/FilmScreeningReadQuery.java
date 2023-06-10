package code.films.application.queries;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FilmScreeningReadQuery(String filmTitle, LocalDateTime date) {}
