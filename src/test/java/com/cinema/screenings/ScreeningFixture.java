package com.cinema.screenings;

import com.cinema.films.domain.Film;
import com.cinema.halls.domain.Hall;
import com.cinema.screenings.domain.Screening;
import com.cinema.tickets.domain.Ticket;
import com.cinema.users.domain.User;

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

    public static Screening createScreeningWithTicket(Film film, Hall hall) {
        var screening = new Screening(
                SCREENING_DATE,
                SCREENING_END_DATE,
                film,
                hall
        );
        var seats = List.of(
                new Ticket(screening, hall.getSeats().getFirst())
        );
        screening.assignTickets(seats);
        return screening;
    }

    public static Screening createScreeningWithTickets(Film film, Hall hall) {
        var screening = new Screening(
                SCREENING_DATE,
                SCREENING_END_DATE,
                film,
                hall
        );
        var seats = List.of(
                new Ticket(screening, hall.getSeats().get(0)),
                new Ticket(screening, hall.getSeats().get(1))
        );
        screening.assignTickets(seats);
        return screening;
    }

    public static Screening createScreeningWithTickets(LocalDateTime date, Film film, Hall hall) {
        var screening = new Screening(
                date,
                SCREENING_END_DATE,
                film,
                hall
        );
        var seats = List.of(
                new Ticket(screening, hall.getSeats().get(0)),
                new Ticket(screening, hall.getSeats().get(1))
        );
        screening.assignTickets(seats);
        return screening;
    }

    public static Screening createScreeningWithBookedTicket(Film film, Hall hall, User user) {
        var screening = new Screening(
                SCREENING_DATE,
                SCREENING_END_DATE,
                film,
                hall
        );
        var seats = List.of(
                new Ticket(screening, hall.getSeats().getFirst(), user)
        );
        screening.assignTickets(seats);
        return screening;
    }
}