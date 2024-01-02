package com.cinema.halls.application.dto;

import java.util.List;

public record CreateHallDto(List<CreateSeatDto> seats) {
}
