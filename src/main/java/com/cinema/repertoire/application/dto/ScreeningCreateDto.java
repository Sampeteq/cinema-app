package com.cinema.repertoire.application.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record ScreeningCreateDto(

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @NotNull
        LocalDateTime date,

        @NotNull
        String filmTitle
) {
}
