package com.cinema.halls.ui;

import com.cinema.halls.application.queries.dto.HallDto;

import java.util.List;

public record HallsResponse(List<HallDto> halls) {
}
