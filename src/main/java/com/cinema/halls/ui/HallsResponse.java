package com.cinema.halls.ui;

import com.cinema.halls.application.dto.HallDto;

import java.util.List;

public record HallsResponse(List<HallDto> halls) {
}
