package code.films;

import code.films.dto.FilmCategoryView;

enum FilmCategory {
    COMEDY,
    DRAMA,
    ACTION,
    THRILLER,
    HORROR,
    FANTASY;

    static FilmCategory fromDTO(FilmCategoryView dto) {
        return FilmCategory.valueOf(dto.name());
    }
}
