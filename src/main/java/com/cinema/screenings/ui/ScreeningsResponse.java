package com.cinema.screenings.ui;

import com.cinema.screenings.application.queries.dto.ScreeningDto;

import java.util.List;

public record ScreeningsResponse(List<ScreeningDto> screenings) {
}
