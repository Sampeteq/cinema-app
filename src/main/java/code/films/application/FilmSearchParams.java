package code.films.application;

import code.films.domain.FilmCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FilmSearchParams {

    public final FilmCategory category;
}
