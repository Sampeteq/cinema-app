package code.bookings.helpers;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingStatus;
import code.catalog.domain.Seat;

import java.time.Clock;
import java.time.ZoneOffset;

public class BookingTestHelper {

    public static Booking createBooking(Seat seat, Long userId) {
        var clock = getClockForBookingOrCancelling(seat);
        return Booking.make(seat, clock, userId);
    }

    public static Booking createBooking(Seat seat, Long userId, BookingStatus status) {
        var clock = getClockForBookingOrCancelling(seat);
        if (status.equals(BookingStatus.CANCELLED)) {
            var booking = createBooking(seat, userId);
            booking.cancel(clock);
            return booking;
        } else {
            throw new IllegalArgumentException("Not supported");
        }
    }

    private static Clock getClockForBookingOrCancelling(Seat seat) {
        var screeningDate = seat
                .getScreening()
                .getDate()
                .minusDays(2)
                .toInstant(ZoneOffset.UTC);
        return Clock.fixed(screeningDate, ZoneOffset.UTC);
    }
}