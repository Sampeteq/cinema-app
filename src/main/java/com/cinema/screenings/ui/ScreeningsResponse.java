package com.cinema.screenings.ui;

import com.cinema.screenings.application.dto.ScreeningView;

import java.util.List;

public record ScreeningsResponse(List<ScreeningView> screenings) {
}
