package com.example.film.domain.dto;

import com.example.film.domain.FilmCategory;
import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record AddFilmDTO(@NotNull String title,
                         @NotNull FilmCategory filmCategory,
                         @NotNull Integer year) {}
