package com.cinema.halls.application.dto;

import java.util.List;

public record HallDto(long id, List<SeatDto> seats) {
}
