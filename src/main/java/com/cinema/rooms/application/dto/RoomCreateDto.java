package com.cinema.rooms.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.With;

@With
public record RoomCreateDto(
        @NotNull
        String id,
        @Positive
        int rowsNumber,
        @Positive
        int rowSeatsNumber
) {
}
