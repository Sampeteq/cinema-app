package com.cinema.screenings;

import com.cinema.films.domain.Film;
import com.cinema.halls.domain.Hall;
import com.cinema.screenings.domain.Screening;
import com.cinema.screenings.domain.ScreeningSeat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public final class ScreeningFixture {
    public static final LocalDateTime SCREENING_DATE = LocalDateTime
            .now()
            .plusDays(8)
            .truncatedTo(ChronoUnit.MINUTES);
    public static final LocalDateTime SCREENING_END_DATE = SCREENING_DATE.plusMinutes(100);

    private ScreeningFixture() {
    }

    public static Screening createScreening(Film film, Hall hall) {
        return new Screening(
                SCREENING_DATE,
                SCREENING_END_DATE,
                film,
                hall
        );
    }

    public static Screening createScreening(LocalDateTime screeningDate, Film film, Hall hall) {
        return new Screening(
                screeningDate,
                SCREENING_END_DATE,
                film,
                hall
        );
    }

    public static Screening createScreeningWithSeats(Hall hall, Film film) {
        var screening = new Screening(
                SCREENING_DATE,
                SCREENING_END_DATE,
                film,
                hall
        );
        var seats = List.of(
                new ScreeningSeat(true, hall.getSeats().get(0), screening),
                new ScreeningSeat(true, hall.getSeats().get(1), screening)
        );
        screening.assignSeats(seats);
        return screening;
    }

    public static Screening createScreeningWithSeats(LocalDateTime date, Film film, Hall hall) {
        var screening = new Screening(
                date,
                SCREENING_END_DATE,
                film,
                hall
        );
        var seats = List.of(
                new ScreeningSeat(true, hall.getSeats().get(0), screening),
                new ScreeningSeat(true, hall.getSeats().get(1), screening)
        );
        screening.assignSeats(seats);
        return screening;
    }

    public static Screening createScreeningWithNotFreeSeat(Hall hall, Film film) {
        var screening = new Screening(
                SCREENING_DATE,
                SCREENING_END_DATE,
                film,
                hall
        );
        var seats = List.of(
                new ScreeningSeat(false, hall.getSeats().getFirst(), screening)
        );
        screening.assignSeats(seats);
        return screening;
    }
}