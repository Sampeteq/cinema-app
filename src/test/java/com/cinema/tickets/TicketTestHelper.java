package com.cinema.tickets;

import com.cinema.MockTimeProvider;
import com.cinema.catalog.application.dto.FilmCreateDto;
import com.cinema.catalog.application.dto.RoomCreateDto;
import com.cinema.catalog.application.dto.ScreeningCreateDto;
import com.cinema.catalog.domain.FilmCategory;
import com.cinema.tickets.domain.Ticket;

import java.time.LocalDateTime;
import java.time.Year;

public final class TicketTestHelper {

    public static final String ROOM_CUSTOM_ID = "1";
    public static final int ROOM_ROWS_NUMBER = 10;
    public static final int ROOM_ROW_SEATS_NUMBER = 15;
    private static final Long FILM_ID = 1L;
    public static final String FILM_TITLE = "Title 1";
    public static final FilmCategory FILM_CATEGORY = FilmCategory.COMEDY;
    public static final int FILM_DURATION_IN_MINUTES = 100;
    public static final LocalDateTime CURRENT_DATE = new MockTimeProvider().getCurrentDate();
    public static final Long SCREENING_ID = 1L;
    public static final LocalDateTime SCREENING_DATE = CURRENT_DATE.plusDays(7);
    public static final int SEAT_NUMBER = 1;
    public static final int ROW_NUMBER = 1;
    public static final long USER_ID = 1L;

    private TicketTestHelper() {
    }

    public static Ticket prepareTicket() {
        var screeningDate = CURRENT_DATE.plusDays(7);
        return prepareTicket(screeningDate);
    }

    public static Ticket prepareTicket(LocalDateTime screeningDate) {
        return new Ticket(
                FILM_TITLE,
                SCREENING_ID,
                screeningDate,
                ROOM_CUSTOM_ID,
                ROW_NUMBER,
                SEAT_NUMBER
        );
    }

    public static Ticket prepareCancelledTicket() {
        var ticket = prepareTicket();
        ticket.book(CURRENT_DATE, USER_ID);
        ticket.cancel(CURRENT_DATE);
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
                FILM_ID
        );
    }

    public static ScreeningCreateDto createScreeningCrateDto(LocalDateTime date) {
        return createScreeningCrateDto().withDate(date);
    }
}
