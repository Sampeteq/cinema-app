package code.bookings.application;

import code.bookings.application.dto.BookingDto;
import code.bookings.application.internal.BookingService;
import code.bookings.application.internal.BookingSearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class BookingFacade {

    private final BookingService bookingService;

    private final BookingSearchService bookingSearchService;

    @Transactional
    public BookingDto bookSeat(UUID seatId, String username) {
        return bookingService.make(seatId, username);
    }

    @Transactional
    public void cancelBooking(UUID bookingId) {
        bookingService.cancel(bookingId);
    }

    public BookingDto searchBookingById(UUID bookingId, String username) {
        return bookingSearchService.searchBookingById(bookingId, username);
    }

    public List<BookingDto> searchAllBookings(String username) {
        return bookingSearchService.searchAllBookings(username);
    }
}
