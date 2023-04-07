package code.films.domain.queries;

import code.films.domain.FilmCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SearchFilmsQuery {

    public final FilmCategory category;
}
