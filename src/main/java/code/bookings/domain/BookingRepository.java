package code.bookings.domain;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking add(Booking booking);
    Optional<Booking> readByIdAndUserId(Long bookingId, Long userId);
    List<Booking> readByUserId(Long userId);
}
