package code.bookings.application;

import code.bookings.application.dto.BookingCancelledEvent;
import code.bookings.application.dto.BookingDto;
import code.bookings.application.dto.SeatBookedEvent;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.infrastructure.exceptions.BookingNotFoundException;
import code.screenings.application.ScreeningFacade;
import com.google.common.eventbus.EventBus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.UUID;

@Component
@AllArgsConstructor
public class Booker {

    private final BookingRepository bookingRepository;

    private final ScreeningFacade screeningFacade;

    private final EventBus eventBus;

    BookingDto bookSeat(UUID seatId, String username, Clock clock) {
        var seatDetails = screeningFacade.searchSeatDetails(seatId, clock);
        var booking = Booking.make(seatId, seatDetails, username);
        eventBus.post(
                new SeatBookedEvent(seatId)
        );
        return bookingRepository
                .save(booking)
                .toDto();
    }

    void cancelSeat(UUID bookingId, Clock clock) {
        var booking = getBookingOrThrow(bookingId);
        var seatId = booking.getSeatId();
        var seatDetails = screeningFacade.searchSeatDetails(seatId, clock);
        booking.cancel(seatDetails);
        eventBus.post(
                new BookingCancelledEvent(seatId)
        );
    }

    private Booking getBookingOrThrow(UUID bookingId) {
        return bookingRepository
                .findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);
    }
}
