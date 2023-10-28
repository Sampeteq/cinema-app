package com.cinema.tickets;

import com.cinema.films.application.commands.CreateFilm;
import com.cinema.screenings.application.commands.CreateScreening;
import com.cinema.films.domain.FilmCategory;
import com.cinema.rooms.application.commands.CreateRoom;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketStatus;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;

public final class TicketFixture {

    public static final String ROOM_CUSTOM_ID = "1";
    public static final int ROOM_ROWS_NUMBER = 10;
    public static final int ROOM_ROW_SEATS_NUMBER = 15;
    public static final Long FILM_ID = 1L;
    public static final String FILM_TITLE = "Title 1";
    public static final FilmCategory FILM_CATEGORY = FilmCategory.COMEDY;
    public static final int FILM_DURATION_IN_MINUTES = 100;
    public static final Long SCREENING_ID = 1L;
    public static final LocalDateTime SCREENING_DATE = LocalDateTime
            .now()
            .plusDays(8)
            .truncatedTo(ChronoUnit.MINUTES);
    public static final long SEAT_ID = 1L;
    public static final long USER_ID = 1L;

    private TicketFixture() {
    }

    public static Ticket createTicket() {
        return new Ticket(
                TicketStatus.ACTIVE,
                SCREENING_ID,
                SEAT_ID,
                USER_ID
        );
    }

    public static Ticket createTicket(Long userId) {
        return new Ticket(
                TicketStatus.ACTIVE,
                SCREENING_ID,
                SEAT_ID,
                userId
        );
    }

    public static Ticket createCancelledTicket() {
        var ticket = createTicket();
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

    public static CreateRoom createCreateRoomCommand() {
        return new CreateRoom(
                ROOM_CUSTOM_ID,
                ROOM_ROWS_NUMBER,
                ROOM_ROW_SEATS_NUMBER
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
}
