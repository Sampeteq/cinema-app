package code.bookings.application;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.BookingStatus;
import code.bookings.domain.dto.BookDto;
import code.bookings.domain.dto.BookingCancelledEvent;
import code.bookings.domain.dto.BookingDto;
import code.bookings.domain.dto.SeatBookedEvent;
import code.bookings.domain.exception.BookingException;
import code.bookings.infrastructure.exception.BookingNotFoundException;
import code.bookings.domain.exception.SeatNotAvailableException;
import code.bookings.domain.exception.TooLateToBookingException;
import code.screenings.application.ScreeningFacade;
import com.google.common.eventbus.EventBus;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.util.UUID;

@AllArgsConstructor
public
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
            throw new TooLateToBookingException();
        }
        if (!screeningDetails.isSeatAvailable()) {
            throw new SeatNotAvailableException();
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
