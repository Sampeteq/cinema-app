package com.cinema.screenings.application.dto;

import java.time.LocalDateTime;

public record ScreeningDetailsDto(
        LocalDateTime date,
        String filmTitle,
        String roomId
) {
}
