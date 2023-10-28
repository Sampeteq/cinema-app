package com.cinema.rooms.application.queries;

import java.time.LocalDateTime;

public record FindFirstAvailableRoom(LocalDateTime start, LocalDateTime end) {
}
