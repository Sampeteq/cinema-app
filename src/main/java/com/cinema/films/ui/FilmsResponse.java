package com.cinema.films.ui;

import com.cinema.films.domain.Film;

import java.util.List;

public record FilmsResponse(List<Film> films) {
}
