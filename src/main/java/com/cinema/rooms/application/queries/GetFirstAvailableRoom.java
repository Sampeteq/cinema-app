package com.cinema.rooms.application.queries;

import java.time.LocalDateTime;

public record GetFirstAvailableRoom(LocalDateTime start, LocalDateTime end) {
}
