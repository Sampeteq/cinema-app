package com.cinema.repertoire.application.dto;

import java.time.LocalDateTime;

public record ScreeningDetailsDto(
        LocalDateTime date,
        String filmTitle,
        String roomId
) {
}
