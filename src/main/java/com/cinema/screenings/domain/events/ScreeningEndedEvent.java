package com.cinema.screenings.domain.events;


import java.time.LocalDateTime;

public record ScreeningEndedEvent(
        String roomId,
        LocalDateTime start,
        LocalDateTime end
) {
}
