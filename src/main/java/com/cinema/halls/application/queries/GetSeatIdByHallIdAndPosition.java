package com.cinema.halls.application.queries;

public record GetSeatIdByHallIdAndPosition(Long hallId, int rowNumber , int number) {
}
