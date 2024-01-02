package com.cinema.films.ui;

import com.cinema.films.application.dto.FilmDto;

import java.util.List;

public record FilmsResponse(List<FilmDto> films) {
}
