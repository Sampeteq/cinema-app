package code.utils;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingStatus;
import code.screenings.domain.Seat;

import java.util.UUID;

public class BookingTestHelper {

    public static Booking createBooking(Seat seat, String username) {
        return new Booking(
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                seat,
                username
        );
    }

    public static Booking createBooking(Seat seat, String username, BookingStatus status) {
        return new Booking(
                UUID.randomUUID(),
                status,
                seat,
                username
        );
    }
}
