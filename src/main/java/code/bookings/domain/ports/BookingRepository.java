package code.bookings.domain.ports;

import code.bookings.domain.Booking;

import java.util.Optional;

public interface BookingRepository {
    Booking add(Booking booking);
    Optional<Booking> readByIdAndUserId(Long bookingId, Long userId);
}
