package com.cinema.halls.infrastructure.config;

public record ConfigHallDto(
        String id,
        int rowsNumber,
        int seatsNumberInOneRow
) {
}
