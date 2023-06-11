package code.bookings.infrastructure.db;

import code.bookings.domain.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking add(Booking booking);
    Optional<Booking> readByIdAndUserId(Long bookingId, Long userId);
    List<Booking> readAllByUserId(Long userId);
    boolean existsBySeatId(Long seatId);
}