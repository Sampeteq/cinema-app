package com.cinema.screenings.domain;

import com.cinema.screenings.application.exceptions.ScreeningsCollisionsException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class ScreeningCollisionsService {

    private final ScreeningRepository screeningRepository;

    public void validate(LocalDateTime date, LocalDateTime endDate, UUID hallId) {
        var collisions = screeningRepository.getCollisions(
                date,
                endDate,
                hallId
        );
        if (!collisions.isEmpty()) {
            throw new ScreeningsCollisionsException();
        }
    }
}
