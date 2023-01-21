package code.bookings;

import code.bookings.dto.BookingDto;
import code.bookings.exception.SeatBookingNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
class BookingSearcher {

    private final BookingRepository bookingRepository;

    BookingDto searchSeatBooking(UUID bookingId, String username) {
        return getBookingOrThrow(bookingId, username).toDto();
    }

    private Booking getBookingOrThrow(UUID ticketId, String username) {
        return bookingRepository
                .findByIdAndUsername(ticketId, username)
                .orElseThrow(SeatBookingNotFoundException::new);
    }

    public List<BookingDto> searchAllByUsername(String username) {
        return bookingRepository
                .findByUsername(username)
                .stream()
                .map(Booking::toDto)
                .toList();
    }
}
