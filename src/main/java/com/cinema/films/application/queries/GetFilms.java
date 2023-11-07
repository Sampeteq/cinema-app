package com.cinema.films.application.queries;

import com.cinema.films.domain.FilmCategory;
import lombok.Builder;

@Builder
public record GetFilms(String title, FilmCategory category) {
}
