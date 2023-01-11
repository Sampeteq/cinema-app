package code.films.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FilmSearchParamsView {

    public final FilmCategoryView category;
}
