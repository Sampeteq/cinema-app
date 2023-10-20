package com.cinema.screenings.domain.events;

import java.time.LocalDateTime;

public record ScreeningEndedEvent(
        LocalDateTime screeningDate,
        String roomId
) {
}
