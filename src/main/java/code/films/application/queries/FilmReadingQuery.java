package code.films.application.queries;

import code.films.domain.FilmCategory;
import lombok.Builder;

@Builder
public record FilmReadingQuery(FilmCategory category) {}
