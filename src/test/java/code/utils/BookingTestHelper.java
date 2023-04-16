package code.utils;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingStatus;
import code.bookings.domain.Seat;

import java.util.List;
import java.util.UUID;

public class BookingTestHelper {

    public static Booking createSampleBooking(Seat seat, String username) {
        return new Booking(
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                seat,
                username
        );
    }

    public static List<Booking> createSampleBookings(Seat seat1, Seat seat2, String username) {
        var booking1 = new Booking(
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                seat1,
                username
        );
        var booking2 = new Booking(
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                seat2,
                username
        );
        return List.of(booking1, booking2);
    }
}
