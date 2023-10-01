package com.cinema.catalog.application.dto;

import java.time.LocalDateTime;

public record ScreeningDetailsDto(
        String filmTitle,
        LocalDateTime date,
        String roomId,
        boolean seatExists
) {
}
