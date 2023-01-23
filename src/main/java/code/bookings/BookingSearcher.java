package code.bookings;

import code.bookings.dto.BookingDto;
import code.bookings.exception.BookingNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
class BookingSearcher {

    private final BookingRepository bookingRepository;

    BookingDto searchBookingById(UUID bookingId, String username) {
        return getBookingOrThrow(bookingId, username).toDto();
    }

    private Booking getBookingOrThrow(UUID ticketId, String username) {
        return bookingRepository
                .findByIdAndUsername(ticketId, username)
                .orElseThrow(BookingNotFoundException::new);
    }

    public List<BookingDto> searchAllBookings(String username) {
        return bookingRepository
                .findByUsername(username)
                .stream()
                .map(Booking::toDto)
                .toList();
    }
}
