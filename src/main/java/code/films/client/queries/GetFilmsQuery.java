package code.films.client.queries;

import code.films.domain.FilmCategory;
import lombok.Builder;

@Builder
public record GetFilmsQuery(FilmCategory category) {}
