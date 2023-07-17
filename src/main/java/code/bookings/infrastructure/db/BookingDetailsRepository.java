package code.bookings.infrastructure.db;

import code.bookings.domain.BookingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingDetailsRepository extends JpaRepository<BookingDetails, Long> {
    Optional<BookingDetails> findByBookingIdAndBookingUserId(Long bookingId, Long userId);
    List<BookingDetails> findAllByBookingUserId(Long userId);
}
