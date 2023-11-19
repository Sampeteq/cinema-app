package com.cinema.halls.application.commands;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateHall(
        @NotNull
        String id,
        @Positive
        int rowsNumber,
        @Positive
        int seatsNumberInOneRow
) {
}
