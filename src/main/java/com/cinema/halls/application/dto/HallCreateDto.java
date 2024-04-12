package com.cinema.halls.application.dto;

import com.cinema.halls.domain.Seat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record HallCreateDto(@NotNull @NotEmpty List<Seat> seats) {
}
