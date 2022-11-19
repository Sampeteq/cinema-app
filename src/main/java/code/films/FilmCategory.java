package code.films;

import code.films.dto.FilmCategoryDTO;

enum FilmCategory {
    COMEDY,
    DRAMA,
    ACTION,
    THRILLER,
    HORROR,
    FANTASY;

    static FilmCategory fromDTO(FilmCategoryDTO dto) {
        return FilmCategory.valueOf(dto.name());
    }
}
