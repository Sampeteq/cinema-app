package code.bookings.domain;

import code.MockTimeProvider;
import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.domain.exceptions.BookingAlreadyExists;
import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.bookings.domain.exceptions.BookingTooLateException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static code.bookings.helpers.BookingTestHelper.prepareBooking;
import static code.bookings.helpers.BookingTestHelper.prepareCancelledBooking;
import static code.bookings.helpers.BookingTestHelper.prepareScreening;
import static code.bookings.helpers.BookingTestHelper.prepareScreeningWithBookedSeat;
import static code.bookings.helpers.BookingTestHelper.prepareSeat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingTests {

    private static final LocalDateTime currentDate = new MockTimeProvider().getCurrentDate();

    @Test
    void booking_is_made() {
        //given
        var seat = prepareSeat();
        var screening = prepareScreening(seat);
        var userId = 1L;

        //when
        var booking = Booking.make(currentDate, screening, seat.getId(), userId);

        //then
        assertThat(booking.getScreening()).isEqualTo(screening);
        assertThat(booking.getSeat()).isEqualTo(seat);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.ACTIVE);
        assertThat(booking.getUserId()).isEqualTo(userId);
        assertThat(booking.getSeat().isFree()).isFalse();
    }

    @Test
    void booking_is_not_duplicated() {
        //given
        var seat = prepareSeat();
        var screening = prepareScreeningWithBookedSeat(seat);
        var seatId = seat.getId();
        var otherUserId = 2L;

        //when
        assertThrows(
                BookingAlreadyExists.class,
                () -> Booking.make(currentDate, screening, seatId, otherUserId)
        );
    }

    @Test
    void booking_is_made_no_later_than_1_hour_before_screening() {
        //given
        var seat = prepareSeat();
        var screeningDate = currentDate.minusMinutes(59);
        var screening = prepareScreening(seat, screeningDate);
        var seatId = seat.getId();
        var userId = 1L;

        //when
        assertThrows(
                BookingTooLateException.class,
                () -> Booking.make(currentDate, screening, seatId, userId)
        );
    }

    @Test
    void booking_is_cancelled() {
        //given
        var booking = prepareBooking();

        //when
        booking.cancel(currentDate);

        //then
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELLED);
        assertThat(booking.getSeat().isFree()).isTrue();
    }

    @Test
    void booking_is_cancelled_no_later_than_24h_hours_before_screening() {
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
    void booking_is_not_canceled_if_it_was_already_cancelled() {
        //given
        var booking = prepareCancelledBooking();

        //when
        assertThrows(
                BookingAlreadyCancelledException.class,
                () -> booking.cancel(currentDate)
        );
    }
}
