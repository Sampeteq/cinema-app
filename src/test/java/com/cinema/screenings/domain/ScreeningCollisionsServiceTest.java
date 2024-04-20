package com.cinema.screenings.domain;

import com.cinema.screenings.application.exceptions.ScreeningsCollisionsException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.cinema.screenings.ScreeningFixtures.SCREENING_DATE;
import static com.cinema.screenings.ScreeningFixtures.SCREENING_END_DATE;
import static com.cinema.screenings.ScreeningFixtures.createScreening;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScreeningCollisionsServiceTest {
    ScreeningRepository screeningRepository = mock();
    ScreeningCollisionsService screeningCollisionsService = new ScreeningCollisionsService(screeningRepository);

    @Test
    void screenings_collision_cannot_exists() {
        when(
                screeningRepository.getCollisions(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        any(UUID.class)
                )
        ).thenReturn(List.of(createScreening()));

        var exception = catchException(
                () -> screeningCollisionsService.validate(
                        SCREENING_DATE,
                        SCREENING_END_DATE,
                        UUID.randomUUID()
                )
        );

        assertEquals(ScreeningsCollisionsException.class, exception.getClass());
    }
}
