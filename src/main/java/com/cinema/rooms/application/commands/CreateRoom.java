package com.cinema.rooms.application.commands;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.With;

@With
public record CreateRoom(
        @NotNull
        String id,
        @Positive
        int rowsNumber,
        @Positive
        int rowSeatsNumber
) {
}
