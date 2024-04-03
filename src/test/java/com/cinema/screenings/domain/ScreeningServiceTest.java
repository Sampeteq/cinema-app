package com.cinema.screenings.domain;

import com.cinema.films.domain.FilmService;
import com.cinema.halls.domain.HallService;
import com.cinema.screenings.domain.exceptions.ScreeningDateOutOfRangeException;
import com.cinema.screenings.domain.exceptions.ScreeningsCollisionsException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.cinema.films.FilmFixtures.createFilm;
import static com.cinema.halls.HallFixtures.createHall;
import static com.cinema.screenings.ScreeningFixtures.createScreening;
import static com.cinema.screenings.ScreeningFixtures.createScreeningCreateDto;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        setCurrentDate(screeningCreateDto.date().minusDays(6));

        var exception = catchException(() -> screeningService.createScreening(screeningCreateDto));

        assertEquals(ScreeningDateOutOfRangeException.class, exception.getClass());
    }

    @Test
    void screening_and_current_date_difference_is_max_21_days() {
        var screeningCreateDto = createScreeningCreateDto();
        setCurrentDate(screeningCreateDto.date().minusDays(22));

        var exception = catchException(() -> screeningService.createScreening(screeningCreateDto));

        assertEquals(ScreeningDateOutOfRangeException.class, exception.getClass());
    }

    @Test
    void screenings_collision_cannot_exists() {
        var screeningCreateDto = createScreeningCreateDto();
        setCurrentDate(screeningCreateDto.date().minusDays(7));
        when(filmService.getFilmById(anyLong())).thenReturn(createFilm());
        when(hallService.getHallById(anyLong())).thenReturn(createHall());
        when(
                screeningRepository.getCollisions(
                        any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        anyLong()
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
