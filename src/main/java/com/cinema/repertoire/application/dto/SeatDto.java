package com.cinema.repertoire.application.dto;

public record SeatDto(
        int rowNumber,
        int number,
        boolean isFree
) {
}
