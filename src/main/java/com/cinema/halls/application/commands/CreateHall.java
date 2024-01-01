package com.cinema.halls.application.commands;

import java.util.List;

public record CreateHall(List<CreateSeatDto> seats) {
}
