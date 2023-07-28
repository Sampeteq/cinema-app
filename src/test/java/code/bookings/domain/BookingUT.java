package code.bookings.domain;

import code.MockTimeProvider;
import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.domain.exceptions.BookingAlreadyExists;
import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.bookings.domain.exceptions.BookingTooLateException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingUT {

    private static final LocalDateTime currentDate = new MockTimeProvider().getCurrentDate();

    @Test
    void should_booked_seat() {
        //given
        var screeningDate = currentDate.plusDays(2);
        var seat = prepareSeat(screeningDate);
        var userId = 1L;

        //when
        seat.book(currentDate, userId);

        //then
        assertThat(seat.isFree()).isFalse();
        assertThat(seat.getBookings()).hasSize(1);
        assertThat(seat.getBookings().get(0).getStatus()).isEqualTo(BookingStatus.ACTIVE);
        assertThat(seat.getBookings().get(0).getUserId()).isEqualTo(userId);
    }

    @Test
    void should_throw_exception_during_booking_when_booking_already_exists() {
        //given
        var seat = prepareBookedSeat();
        var otherUserId = 2L;

        //when
        assertThrows(
                BookingAlreadyExists.class,
                () -> seat.book(currentDate, otherUserId)
        );
    }

    @Test
    void should_throw_exception_during_booking_when_less_than_1_hour_to_screening() {
        //given
        var screeningDate = currentDate.minusMinutes(59);
        var seat = prepareSeat(screeningDate);
        var userId = 1L;

        //when
        assertThrows(
                BookingTooLateException.class,
                () -> seat.book(currentDate, userId)
        );
    }

    @Test
    void should_cancel_booking() {
        //given
        var booking = prepareBooking();

        //when
        booking.cancel(currentDate);

        //then
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELLED);
        assertThat(booking.getSeat().isFree()).isTrue();
    }

    @Test
    void should_throw_exception_during_booking_cancelling_when_less_than_24_hours_to_screening() {
        //given
        var screeningDate = currentDate.minusHours(23);
        var booking = prepareBooking(screeningDate);

        //when
        assertThrows(
                BookingCancelTooLateException.class,
                () -> booking.cancel(currentDate)
        );
    }

    @Test
    void should_throw_exception_during_booking_cancelling_when_booking_is_already_cancelled() {
        //given
        var booking = prepareCancelledBooking();

        //when
        assertThrows(
                BookingAlreadyCancelledException.class,
                () -> booking.cancel(currentDate)
        );
    }

    private Seat prepareSeat(LocalDateTime screeningDate) {
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        var seats = List.of(Seat.create(rowNumber, seatNumber));
        return Screening
                .create(screeningId, screeningDate, seats)
                .getSeats()
                .get(0);
    }


    private Seat prepareBookedSeat() {
        var screeningId = 1L;
        var screeningDate = currentDate.plusDays(7);
        var rowNumber = 1;
        var seatNumber = 1;
        var seats = List.of(Seat.create(rowNumber, seatNumber));
        var seat = Screening
                .create(screeningId, screeningDate, seats)
                .getSeats()
                .get(0);
        var userId = 1L;
        seat.book(currentDate, userId);
        return seat;
    }

    private Booking prepareBooking() {
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        var seats = List.of(Seat.create(rowNumber, seatNumber));
        var screeningDate = currentDate.plusDays(2);
        var seat = Screening
                .create(screeningId, screeningDate, seats)
                .getSeats()
                .get(0);
        var userId = 1L;
        return Booking.make(seat, userId);
    }

    private Booking prepareBooking(LocalDateTime screeningDate) {
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        var seats = List.of(Seat.create(rowNumber, seatNumber));
        var seat = Screening
                .create(screeningId, screeningDate, seats)
                .getSeats()
                .get(0);
        var userId = 1L;
        return Booking.make(seat, userId);
    }

    private Booking prepareCancelledBooking() {
        var screeningId = 1L;
        var screeningDate = currentDate.plusDays(7);
        var rowNumber = 1;
        var seatNumber = 1;
        var seats = List.of(Seat.create(rowNumber, seatNumber));
        var seat = Screening
                .create(screeningId, screeningDate, seats)
                .getSeats()
                .get(0);
        var userId = 1L;
        var booking = Booking.make(seat, userId);
        booking.cancel(currentDate);
        return booking;
    }
}
