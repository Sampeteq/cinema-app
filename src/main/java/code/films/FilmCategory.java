package code.films;

import code.films.dto.FilmCategoryDto;

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
