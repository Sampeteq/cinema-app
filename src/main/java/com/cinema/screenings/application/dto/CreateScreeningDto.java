package com.cinema.screenings.application.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record CreateScreeningDto(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @NotNull
        LocalDateTime date,
        @NotNull
        Long filmId,
        @NotNull
        Long hallId
) {
}
