package code.films.dto;

import code.films.FilmCategory;

public record FilmDTO(Long id, String title, FilmCategory category, int year) {
}
