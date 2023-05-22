package code.bookings.domain;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking add(Booking booking);
    List<Booking> addMany(List<Booking> bookings);
    Optional<Booking> readById(Long bookingId);
    Optional<Booking> readByIdAndUserId(Long bookingId, Long userId);
    List<Booking> readByUserId(Long userId);
}
