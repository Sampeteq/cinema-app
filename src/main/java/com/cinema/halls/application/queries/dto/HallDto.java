package com.cinema.halls.application.queries.dto;

import java.util.List;

public record HallDto(long id, List<SeatDto> seats) {
}
