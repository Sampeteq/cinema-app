package com.cinema.films.application.queries;

import com.cinema.films.domain.FilmCategory;
import lombok.Builder;

@Builder
public record ReadFilms(String title, FilmCategory category) {
}
