package code.bookings;

import code.MockTimeProvider;
import code.bookings.domain.Booking;
import code.bookings.domain.Screening;
import code.bookings.domain.Seat;
import code.catalog.application.dto.FilmCreateDto;
import code.catalog.application.dto.RoomCreateDto;
import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.domain.FilmCategory;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public final class BookingTestHelper {

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
    private static final long SEAT_ID = 1L;
    private static final int SEAT_ROW_NUMBER = 1;
    private static final int SEAT_NUMBER = 1;
    public static final long USER_ID = 1L;

    private BookingTestHelper() {
    }

    public static Seat prepareSeat() {
        return Seat.create(SEAT_ID, SEAT_ROW_NUMBER, SEAT_NUMBER);
    }

    public static Screening prepareScreening(Seat seat) {
        return prepareScreening(seat, SCREENING_DATE);
    }

    public static Screening prepareScreening(Seat seat, LocalDateTime screeningDate) {
        return Screening.create(SCREENING_ID, screeningDate, List.of(seat));
    }

    public static Screening prepareScreeningWithBookedSeat(Seat seat) {
        var screening = prepareScreening(seat);
        Booking.make(currentDate, screening, seat.getRowNumber(), seat.getNumber(), USER_ID);
        return screening;
    }

    public static Booking prepareBooking() {
        var screeningDate = currentDate.plusDays(7);
        return prepareBooking(screeningDate);
    }

    public static Booking prepareBooking(LocalDateTime screeningDate) {
        var seat = prepareSeat();
        var screening = prepareScreening(seat, screeningDate);
        return Booking.make(currentDate, screening, seat.getNumber(), seat.getRowNumber(), USER_ID);
    }

    public static Booking prepareCancelledBooking() {
        var booking = prepareBooking();
        booking.cancel(currentDate);
        return booking;
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
