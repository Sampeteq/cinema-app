package code.films.domain.queries;

import code.films.domain.FilmCategory;
import lombok.Builder;

@Builder
public record SearchFilmsQuery(FilmCategory category) {}
