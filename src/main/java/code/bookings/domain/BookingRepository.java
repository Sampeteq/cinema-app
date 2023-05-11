package code.bookings.domain;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking add(Booking booking);
    List<Booking> addMany(List<Booking> bookings);
    Optional<Booking> readById(Long bookingId);
    Optional<Booking> readByIdAndUsername(Long ticketId, String username);
    List<Booking> readByUsername(String username);
}
