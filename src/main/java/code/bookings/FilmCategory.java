package code.bookings;

import code.bookings.dto.FilmCategoryDto;

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
