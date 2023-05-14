package code.utils;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingStatus;
import code.screenings.domain.Seat;

public class BookingTestHelper {

    public static Booking createBooking(Seat seat, Long userId) {
        return new Booking(
                1L,
                BookingStatus.ACTIVE,
                seat,
                userId
        );
    }

    public static Booking createBooking(Seat seat, Long userId, BookingStatus status) {
        return new Booking(
                1L,
                status,
                seat,
                userId
        );
    }
}
