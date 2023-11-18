package com.cinema.screenings.application.queries;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GetScreenings(LocalDate date) {
}
