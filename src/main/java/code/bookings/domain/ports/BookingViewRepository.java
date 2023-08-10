package code.bookings.domain.ports;

import code.bookings.domain.BookingView;

import java.util.List;
import java.util.Optional;

public interface BookingViewRepository {
    BookingView add(BookingView bookingView);
    Optional<BookingView> readById(Long id);
    Optional<BookingView> readByIdAndUserId(Long id, Long userId);
    List<BookingView> readAllByUserId(Long userId);
}
