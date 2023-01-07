package code.screenings;

import code.screenings.dto.FilmCategoryDto;

enum FilmCategory {
    COMEDY,
    DRAMA,
    ACTION,
    THRILLER,
    HORROR,
    FANTASY;

    static FilmCategory fromDTO(FilmCategoryDto dto) {
        return FilmCategory.valueOf(dto.name());
    }
}
