package com.cinema.repertoire.domain.events;


import java.time.LocalDateTime;

public record ScreeningEndedEvent(
        String roomId,
        LocalDateTime start,
        LocalDateTime end
) {
}
