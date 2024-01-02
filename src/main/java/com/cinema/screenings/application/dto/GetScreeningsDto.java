package com.cinema.screenings.application.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GetScreeningsDto(LocalDate date) {
}
