package code.bookings.application;

import code.bookings.application.dto.BookingDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class BookingFacade {

    private final Booker booker;

    private final BookingSearcher bookingSearcher;

    @Transactional
    public BookingDto bookSeat(UUID seatId, String username, Clock clock) {
        return booker.bookSeat(seatId, username, clock);
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
