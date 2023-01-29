package code.bookings.application;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.dto.BookingDto;
import code.bookings.infrastructure.exception.BookingNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class BookingSearcher {

    private final BookingRepository bookingRepository;

    public BookingDto searchBookingById(UUID bookingId, String username) {
        return getBookingOrThrow(bookingId, username).toDto();
    }

    public List<BookingDto> searchAllBookings(String username) {
        return bookingRepository
                .findByUsername(username)
                .stream()
                .map(Booking::toDto)
                .toList();
    }

    private Booking getBookingOrThrow(UUID ticketId, String username) {
        return bookingRepository
                .findByIdAndUsername(ticketId, username)
                .orElseThrow(BookingNotFoundException::new);
    }
}
