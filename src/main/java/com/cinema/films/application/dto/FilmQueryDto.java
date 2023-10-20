package com.cinema.films.application.dto;

import com.cinema.films.domain.FilmCategory;
import lombok.Builder;

@Builder
public record FilmQueryDto(String title, FilmCategory category) {
}
