package com.cinema.screenings;

import com.cinema.films.domain.Film;
import com.cinema.halls.domain.Hall;
import com.cinema.screenings.application.dto.ScreeningCreateDto;
import com.cinema.screenings.domain.Screening;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.cinema.ClockFixtures.CURRENT_DATE;
import static com.cinema.screenings.domain.ScreeningConstants.MIN_DAYS_BEFORE_SCREENING;

public class ScreeningFixtures {

    public static final LocalDateTime SCREENING_DATE = CURRENT_DATE.plusDays(MIN_DAYS_BEFORE_SCREENING);

    public static final LocalDateTime SCREENING_END_DATE = SCREENING_DATE.plusHours(2);

    public static ScreeningCreateDto createScreeningCreateDto() {
        var filmId = UUID.randomUUID();
        var hallI = UUID.randomUUID();
        return new ScreeningCreateDto(
                SCREENING_DATE,
                filmId,
                hallI
        );
    }

    public static ScreeningCreateDto createScreeningCreateDto(UUID filmId, UUID hallId) {
        return new ScreeningCreateDto(
                SCREENING_DATE,
                filmId,
                hallId
        );
    }

    public static Screening createScreening() {
        return new Screening(
                UUID.randomUUID(),
                SCREENING_DATE,
                SCREENING_END_DATE,
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }

    public static Screening createScreening(Film film, Hall hall) {
        return new Screening(
                UUID.randomUUID(),
                SCREENING_DATE,
                SCREENING_END_DATE,
                film.getId(),
                hall.getId()
        );
    }

    public static Screening createScreening(LocalDateTime screeningDate, Film film, Hall hall) {
        return new Screening(
                UUID.randomUUID(),
                screeningDate,
                SCREENING_END_DATE,
                film.getId(),
                hall.getId()
        );
    }
}