package com.cinema.halls.application.queries;

import java.time.LocalDateTime;

public record GetFirstAvailableHall(LocalDateTime start, LocalDateTime end) {
}
