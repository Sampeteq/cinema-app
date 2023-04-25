package code.bookings.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository {
    Booking add(Booking booking);
    List<Booking> addMany(List<Booking> bookings);
    Optional<Booking> readById(UUID bookingId);
    Optional<Booking> readByIdAndUsername(UUID ticketId, String username);
    List<Booking> readByUsername(String username);
}
