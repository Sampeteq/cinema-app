package code.films.domain.client.queries;

import code.films.domain.FilmCategory;
import lombok.Builder;

@Builder
public record GetFilmsQuery(FilmCategory category) {}
