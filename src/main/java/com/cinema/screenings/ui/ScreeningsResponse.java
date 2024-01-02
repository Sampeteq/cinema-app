package com.cinema.screenings.ui;

import com.cinema.screenings.application.dto.ScreeningDto;

import java.util.List;

public record ScreeningsResponse(List<ScreeningDto> screenings) {
}
