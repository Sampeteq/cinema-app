package com.cinema.halls.application.queries.dto;

import java.util.List;

public record HallDto(Long id, List<SeatDto> seats) {
}
