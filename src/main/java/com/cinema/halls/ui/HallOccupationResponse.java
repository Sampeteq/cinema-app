package com.cinema.halls.ui;

import com.cinema.halls.application.queries.dto.HallOccupationDto;

import java.util.List;

public record HallOccupationResponse(List<HallOccupationDto> hallsOccupations) {
}
