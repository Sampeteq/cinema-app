package code.screenings.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FilmSearchParamsDto {

    public final FilmCategoryDto category;
}
