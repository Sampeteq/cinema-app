package com.cinema.screenings.application;

import com.cinema.films.application.FilmService;
import com.cinema.halls.application.HallService;
import com.cinema.screenings.application.exceptions.ScreeningsCollisionsException;
import com.cinema.screenings.domain.ScreeningRepository;
import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static com.cinema.films.FilmFixtures.createFilm;
import static com.cinema.halls.HallFixtures.createHall;
import static com.cinema.screenings.ScreeningFixtures.createScreening;
import static com.cinema.screenings.ScreeningFixtures.createScreeningCreateDto;
import static com.cinema.screenings.domain.ScreeningConstants.MAX_DAYS_BEFORE_SCREENING;
import static com.cinema.screenings.domain.ScreeningConstants.MIN_DAYS_BEFORE_SCREENING;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScreeningServiceTest {

    ScreeningRepository screeningRepository = mock();

    HallService hallService = mock();

    FilmService filmService = mock();

    Clock clock = mock();

    ScreeningService screeningService = new ScreeningService(
            screeningRepository,
            hallService,
            filmService,
            clock
    );

    @Test
    void screening_and_current_date_difference_is_min_7_days() {
        var screeningCreateDto = createScreeningCreateDto();
        setCurrentDate(screeningCreateDto.date().minusDays(MIN_DAYS_BEFORE_SCREENING - 1));

        var exception = catchException(() -> screeningService.createScreening(screeningCreateDto));

        assertEquals(ScreeningDateOutOfRangeException.class, exception.getClass());
    }

    @Test
    void screening_and_current_date_difference_is_max_21_days() {
        var screeningCreateDto = createScreeningCreateDto();
        setCurrentDate(screeningCreateDto.date().minusDays(MAX_DAYS_BEFORE_SCREENING + 1));

        var exception = catchException(() -> screeningService.createScreening(screeningCreateDto));

        assertEquals(ScreeningDateOutOfRangeException.class, exception.getClass());
    }

    @Test
    void screenings_collision_cannot_exists() {
        var screeningCreateDto = createScreeningCreateDto();
        setCurrentDate(screeningCreateDto.date().minusDays(7));
        when(filmService.getFilmById(any(UUID.class))).thenReturn(createFilm());
        when(hallService.getHallById(any(UUID.class))).thenReturn(createHall());
        when(
                screeningRepository.getCollisions(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        any(UUID.class)
                )
        ).thenReturn(List.of(createScreening()));

        var exception = catchException(() -> screeningService.createScreening(screeningCreateDto));

        assertEquals(ScreeningsCollisionsException.class, exception.getClass());
    }

    private void setCurrentDate(LocalDateTime date) {
        when(clock.instant()).thenReturn(date.toInstant(ZoneOffset.UTC));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    }
}
