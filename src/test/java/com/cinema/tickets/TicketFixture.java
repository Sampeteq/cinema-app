package com.cinema.tickets;

import com.cinema.films.application.commands.CreateFilm;
import com.cinema.screenings.application.commands.CreateScreening;
import com.cinema.films.domain.FilmCategory;
import com.cinema.halls.application.commands.CreateHall;
import com.cinema.tickets.domain.Seat;
import com.cinema.tickets.domain.SeatStatus;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketStatus;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;

public final class TicketFixture {

    public static final String HALL_CUSTOM_ID = "1";
    public static final int HALL_ROWS_NUMBER = 10;
    public static final int HALL_ROW_SEATS_NUMBER = 15;
    public static final Long FILM_ID = 1L;
    public static final String FILM_TITLE = "Title 1";
    public static final FilmCategory FILM_CATEGORY = FilmCategory.COMEDY;
    public static final int FILM_DURATION_IN_MINUTES = 100;
    public static final Long SCREENING_ID = 1L;
    public static final LocalDateTime SCREENING_DATE = LocalDateTime
            .now()
            .plusDays(8)
            .truncatedTo(ChronoUnit.MINUTES);
    public static final int SEAT_ROW_NUMBER = 1;
    public static final int SEAT_NUMBER = 1;
    public static final long USER_ID = 1L;

    private TicketFixture() {
    }

    public static Ticket createTicket(Seat seat) {
        return new Ticket(
                TicketStatus.BOOKED,
                seat,
                USER_ID
        );
    }

    public static Ticket createTicket(Long userId, Seat seat) {
        return new Ticket(
                TicketStatus.BOOKED,
                seat,
                userId
        );
    }

    public static Ticket createCancelledTicket(Seat seat) {
        var ticket = createTicket(seat);
        ticket.cancel();
        return ticket;
    }

    public static CreateFilm createCreateFilmCommand() {
        var year = Year.now().getValue();
        return new CreateFilm(
                FILM_TITLE,
                FILM_CATEGORY,
                year,
                FILM_DURATION_IN_MINUTES
        );
    }

    public static CreateFilm createCreateFilmCommand(String filmTitle) {
        var year = Year.now().getValue();
        return new CreateFilm(
                filmTitle,
                FILM_CATEGORY,
                year,
                FILM_DURATION_IN_MINUTES
        );
    }

    public static CreateHall createCreateHallCommand() {
        return new CreateHall(
                HALL_CUSTOM_ID,
                HALL_ROWS_NUMBER,
                HALL_ROW_SEATS_NUMBER
        );
    }

    public static CreateHall createCreateHallCommand(String hallId) {
        return new CreateHall(
                hallId,
                HALL_ROWS_NUMBER,
                HALL_ROW_SEATS_NUMBER
        );
    }

    public static CreateScreening createCreateScreeningCommand() {
        return new CreateScreening(
                SCREENING_DATE,
                FILM_ID
        );
    }

    public static CreateScreening createCreateScreeningCommand(LocalDateTime date) {
        return new CreateScreening(
                date,
                FILM_ID
        );
    }

    public static Seat createSeat() {
        return new Seat(SEAT_ROW_NUMBER, SEAT_NUMBER, SeatStatus.FREE, SCREENING_ID);
    }

    public static Seat createSeat(SeatStatus status) {
        return new Seat(SEAT_ROW_NUMBER, SEAT_NUMBER, status, SCREENING_ID);
    }
}
