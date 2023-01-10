package code.screenings;

import code.screenings.dto.FilmCategoryView;

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
