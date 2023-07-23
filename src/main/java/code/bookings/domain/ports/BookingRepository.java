package code.bookings.domain.ports;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingStatus;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking add(Booking booking);
    Optional<Booking> readByIdAndUserId(Long bookingId, Long userId);
    List<Booking> readAllByUserId(Long userId);
    boolean existsBySeatIdAndBookingStatus(Long seatId, BookingStatus bookingStatus);
}
