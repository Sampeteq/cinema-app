package com.cinema.screenings.domain.events;

import com.cinema.rooms.application.queries.dto.RoomDto;

import java.time.LocalDateTime;

public record ScreeningCreatedEvent(
        Long screeningId,
        LocalDateTime start,
        LocalDateTime end,
        RoomDto room
) {
}
