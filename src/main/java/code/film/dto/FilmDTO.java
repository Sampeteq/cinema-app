package code.film.dto;

import code.film.FilmCategory;

public record FilmDTO(Long id, String title, FilmCategory category, int year) {
}
