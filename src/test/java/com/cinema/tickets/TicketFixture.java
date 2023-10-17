package com.cinema.tickets;

import com.cinema.ClockMockConfig;
import com.cinema.repertoire.application.dto.FilmCreateDto;
import com.cinema.repertoire.application.dto.ScreeningCreateDto;
import com.cinema.repertoire.domain.FilmCategory;
import com.cinema.rooms.application.dto.RoomCreateDto;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.TicketStatus;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Year;

public final class TicketFixture {

    public static final String ROOM_CUSTOM_ID = "1";
    public static final int ROOM_ROWS_NUMBER = 10;
    public static final int ROOM_ROW_SEATS_NUMBER = 15;
    public static final String FILM_TITLE = "Title 1";
    public static final FilmCategory FILM_CATEGORY = FilmCategory.COMEDY;
    public static final int FILM_DURATION_IN_MINUTES = 100;
    public static final Clock CLOCK = new ClockMockConfig().clockMock();
    public static final Long SCREENING_ID = 1L;
    public static final LocalDateTime SCREENING_DATE = LocalDateTime.now(CLOCK).plusDays(7);
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

    public static FilmCreateDto createFilmCreateDto() {
        var year = Year.now().getValue();
        return new FilmCreateDto(
                FILM_TITLE,
                FILM_CATEGORY,
                year,
                FILM_DURATION_IN_MINUTES
        );
    }

    public static RoomCreateDto createRoomCreateDto() {
        return new RoomCreateDto(
                ROOM_CUSTOM_ID,
                ROOM_ROWS_NUMBER,
                ROOM_ROW_SEATS_NUMBER
        );
    }

    public static ScreeningCreateDto createScreeningCrateDto() {
        return new ScreeningCreateDto(
                SCREENING_DATE,
                FILM_TITLE
        );
    }

    public static ScreeningCreateDto createScreeningCrateDto(LocalDateTime date) {
        return new ScreeningCreateDto(
                date,
                FILM_TITLE
        );
    }
}
