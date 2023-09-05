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

    private BookingTestHelper() {
    }

    private static final LocalDateTime currentDate = new MockTimeProvider().getCurrentDate();

    public static Seat prepareSeat() {
        var seatId = 1L;
        var seatRowNumber = 1;
        var seatNumber = 1;
        return Seat.create(seatId, seatRowNumber, seatNumber);
    }

    public static Screening prepareScreening(Seat seat) {
        var screeningDate = currentDate.plusDays(7);
        return prepareScreening(seat, screeningDate);
    }

    public static Screening prepareScreening(Seat seat, LocalDateTime screeningDate) {
        var screeningId = 1L;
        return Screening.create(screeningId, screeningDate, List.of(seat));
    }

    public static Screening prepareScreeningWithBookedSeat(Seat seat) {
        var screening = prepareScreening(seat);
        var userId = 1L;
        return Booking
                .make(currentDate, screening, seat.getRowNumber(), seat.getNumber(), userId)
                .getScreening();
    }

    public static Booking prepareBooking() {
        var screeningDate = currentDate.plusDays(7);
        return prepareBooking(screeningDate);
    }

    public static Booking prepareBooking(LocalDateTime screeningDate) {
        var seat = prepareSeat();
        var screening = prepareScreening(seat, screeningDate);
        var userId = 1L;
        return Booking.make(currentDate, screening, seat.getNumber(), seat.getRowNumber(), userId);
    }

    public static Booking prepareCancelledBooking() {
        var booking = prepareBooking();
        booking.cancel(currentDate);
        return booking;
    }

    public static FilmCreateDto createFilmCreateDto() {
        var tile = "Title 1";
        var category = FilmCategory.COMEDY;
        var year = Year.now().getValue();
        var durationInMinutes = 100;
        return new FilmCreateDto(
                tile,
                category,
                year,
                durationInMinutes
        );
    }

    public static RoomCreateDto createRoomCreateDto() {
        var customId = "1";
        var rowsNumber = 10;
        var seatsInOneRowsNumber = 15;
        return new RoomCreateDto(
                customId,
                rowsNumber,
                seatsInOneRowsNumber
        );
    }

    public static ScreeningCreateDto createScreeningCrateDto() {
        var date = LocalDateTime.of(2023, 10, 1, 16, 30);
        var filmId = 1L;
        return new ScreeningCreateDto(
                date,
                filmId
        );
    }

    public static ScreeningCreateDto createScreeningCrateDto(LocalDateTime date) {
        return createScreeningCrateDto().withDate(date);
    }
}
