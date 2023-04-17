package code.utils;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingSeat;
import code.bookings.domain.BookingStatus;
import code.films.domain.Seat;

import java.time.*;
import java.util.List;
import java.util.UUID;

public class BookingTestHelper {

    private static final Instant dateTime = LocalDateTime
            .of(Year.now().getValue(), 5, 8, 18, 30)
            .toInstant(ZoneOffset.UTC);

    private static final Clock clock = Clock.fixed(
            dateTime,
            ZoneOffset.UTC
    );

    public static Booking createSampleBooking(Seat seat, String username) {
        var bookingSeat = BookingSeat
                .builder()
                .id(seat.getId())
                .isAvailable(seat.isFree())
                .timeToScreeningInHours(seat.getScreening().timeToScreeningStartInHours(clock))
                .build();
        return new Booking(
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                bookingSeat,
                username
        );
    }

    public static List<Booking> createSampleBookings(Seat seat1, Seat seat2, String username) {
        var bookingSeat1 = BookingSeat
                .builder()
                .id(seat1.getId())
                .isAvailable(seat1.isFree())
                .timeToScreeningInHours(seat1.getScreening().timeToScreeningStartInHours(clock))
                .build();
        var bookingSeat2 = BookingSeat
                .builder()
                .id(seat2.getId())
                .isAvailable(seat2.isFree())
                .timeToScreeningInHours(seat2.getScreening().timeToScreeningStartInHours(clock))
                .build();
        var booking1 = new Booking(
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                bookingSeat1,
                username
        );
        var booking2 = new Booking(
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                bookingSeat2,
                username
        );
        return List.of(booking1, booking2);
    }
}
