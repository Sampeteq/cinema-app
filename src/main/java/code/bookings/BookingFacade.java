package code.bookings;

import code.bookings.dto.*;
import code.screenings.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class BookingFacade {

    private final Booker booker;

    private final BookingSearcher bookingSearcher;

    @Transactional
    public BookingDto bookSeat(BookDto dto, String username, Clock clock) {
        return booker.bookSeat(dto, username, clock);
    }

    @Transactional
    public void cancelBooking(UUID bookingId, Clock clock) {
        booker.cancelSeat(bookingId, clock);
    }

    public BookingDto searchBookingById(UUID bookingId, String username) {
        return bookingSearcher.searchBookingById(bookingId, username);
    }

    public List<BookingDto> searchAllBookings(String username) {
        return bookingSearcher.searchAllBookings(username);
    }
}
