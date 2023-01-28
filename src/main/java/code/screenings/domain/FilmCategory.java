package code.screenings.domain;

import code.screenings.domain.dto.FilmCategoryDto;

public enum FilmCategory {
    COMEDY,
    DRAMA,
    ACTION,
    THRILLER,
    HORROR,
    FANTASY;

    public static FilmCategory fromDTO(FilmCategoryDto dto) {
        return FilmCategory.valueOf(dto.name());
    }
}
