package code.bookings;

import code.bookings.dto.BookDto;
import code.bookings.dto.BookingCancelledEvent;
import code.bookings.dto.BookingDto;
import code.bookings.dto.SeatBookedEvent;
import code.bookings.exception.BookingException;
import code.bookings.exception.BookingNotFoundException;
import code.screenings.ScreeningFacade;
import com.google.common.eventbus.EventBus;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.util.UUID;

@AllArgsConstructor
class Booker {

    private final BookingRepository bookingRepository;

    private final ScreeningFacade screeningFacade;

    private final EventBus eventBus;

    BookingDto bookSeat(BookDto dto, String username, Clock clock) {
        var screeningDetails = screeningFacade.searchScreeningDetails(
                dto.screeningId(),
                dto.seatId(),
                clock
        );
        if (screeningDetails.timeToScreeningInHours() < 24) {
            throw new BookingException("Too late to booking");
        }
        if (!screeningDetails.isSeatAvailable()) {
            throw new BookingException("Seat not available");
        }
        eventBus.post(
                new SeatBookedEvent(dto.screeningId(), dto.seatId())
        );
        var booking = new Booking(
                UUID.randomUUID(),
                dto.firstName(),
                dto.lastName(),
                BookingStatus.ACTIVE,
                dto.screeningId(),
                dto.seatId(),
                username
        );
        return bookingRepository
                .save(booking)
                .toDto();
    }

    void cancelSeat(UUID bookingId, Clock clock) {
        var booking = getBookingOrThrow(bookingId);
        var screeningId = booking.getScreeningId();
        var seatId = booking.getSeatId();
        var screeningDetails = screeningFacade.searchScreeningDetails(screeningId, seatId, clock);
        if (screeningDetails.timeToScreeningInHours() < 24) {
            throw new BookingException("Too late to cancel booking");
        }
        booking.changeStatus(BookingStatus.CANCELLED);
        eventBus.post(
                new BookingCancelledEvent(
                        screeningId,
                        seatId
                )
        );
    }

    private Booking getBookingOrThrow(UUID bookingId) {
        return bookingRepository
                .findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);
    }
}
