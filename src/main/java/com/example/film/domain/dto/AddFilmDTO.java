package com.example.film.domain.dto;

import com.example.film.domain.FilmCategory;
import lombok.Builder;

import javax.validation.constraints.NotBlank;

@Builder
public record AddFilmDTO(
        @NotBlank(message = "empty") String title,
        FilmCategory filmCategory) {}
