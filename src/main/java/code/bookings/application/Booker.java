package code.bookings.application;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.BookingStatus;
import code.bookings.application.dto.BookingCancelledEvent;
import code.bookings.application.dto.BookingDto;
import code.bookings.application.dto.SeatBookedEvent;
import code.bookings.domain.exceptions.BookingException;
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
        var screeningDetails = screeningFacade.searchSeatDetails(seatId, clock);
        if (screeningDetails.timeToScreeningInHours() < 24) {
            throw new BookingException("Too late to booking");
        }
        if (!screeningDetails.isSeatAvailable()) {
            throw new BookingException("Seat not available");
        }
        eventBus.post(
                new SeatBookedEvent(seatId)
        );
        var booking = new Booking(
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                seatId,
                username
        );
        return bookingRepository
                .save(booking)
                .toDto();
    }

    void cancelSeat(UUID bookingId, Clock clock) {
        var booking = getBookingOrThrow(bookingId);
        var seatId = booking.getSeatId();
        var screeningDetails = screeningFacade.searchSeatDetails(seatId, clock);
        if (screeningDetails.timeToScreeningInHours() < 24) {
            throw new BookingException("Too late to cancel booking");
        }
        booking.cancel();
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
