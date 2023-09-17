package com.cinema.tickets;

import com.cinema.MockTimeProvider;
import com.cinema.tickets.domain.Ticket;
import com.cinema.tickets.domain.Screening;
import com.cinema.tickets.domain.Seat;
import com.cinema.catalog.application.dto.FilmCreateDto;
import com.cinema.catalog.application.dto.RoomCreateDto;
import com.cinema.catalog.application.dto.ScreeningCreateDto;
import com.cinema.catalog.domain.FilmCategory;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public final class TicketTestHelper {

    public static final String ROOM_CUSTOM_ID = "1";
    public static final int ROOM_ROWS_NUMBER = 10;
    public static final int ROOM_ROW_SEATS_NUMBER = 15;
    private static final Long FILM_ID = 1L;
    public static final String FILM_TITLE = "Title 1";
    public static final FilmCategory FILM_CATEGORY = FilmCategory.COMEDY;
    public static final int FILM_DURATION_IN_MINUTES = 100;
    public static final long SCREENING_ID = 1L;
    private static final LocalDateTime currentDate = new MockTimeProvider().getCurrentDate();
    public static final LocalDateTime SCREENING_DATE = currentDate.plusDays(7);
    private static final int SEAT_ROW_NUMBER = 1;
    private static final int SEAT_NUMBER = 1;
    public static final long USER_ID = 1L;

    private TicketTestHelper() {
    }

    public static Seat prepareSeat() {
        return Seat.create(SEAT_ROW_NUMBER, SEAT_NUMBER);
    }

    public static Screening prepareScreening(Seat seat) {
        return prepareScreening(seat, SCREENING_DATE);
    }

    public static Screening prepareScreening(Seat seat, LocalDateTime screeningDate) {
        return Screening.create(
                SCREENING_ID,
                screeningDate,
                FILM_TITLE,
                ROOM_CUSTOM_ID,
                List.of(seat)
        );
    }

    public static Screening prepareScreeningWithBookedSeat(Seat seat) {
        var screening = prepareScreening(seat);
        Ticket.book(currentDate, screening, seat.getRowNumber(), seat.getNumber(), USER_ID);
        return screening;
    }

    public static Ticket prepareTicket() {
        var screeningDate = currentDate.plusDays(7);
        return prepareTicket(screeningDate);
    }

    public static Ticket prepareTicket(LocalDateTime screeningDate) {
        var seat = prepareSeat();
        var screening = prepareScreening(seat, screeningDate);
        return Ticket.book(currentDate, screening, seat.getNumber(), seat.getRowNumber(), USER_ID);
    }

    public static Ticket prepareCancelledTicket() {
        var ticket = prepareTicket();
        ticket.cancel(currentDate);
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
