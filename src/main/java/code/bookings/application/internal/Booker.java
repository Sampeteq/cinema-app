package code.bookings.application.internal;

import code.bookings.application.dto.BookingCancelledEvent;
import code.bookings.application.dto.BookingDto;
import code.bookings.application.dto.SeatBookedEvent;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.BookingScreening;
import code.bookings.domain.BookingSeat;
import code.bookings.infrastructure.exceptions.BookingNotFoundException;
import code.films.application.FilmFacade;
import com.google.common.eventbus.EventBus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.UUID;

@Component
@AllArgsConstructor
public class Booker {

    private final BookingRepository bookingRepository;

    private final FilmFacade filmFacade;

    private final EventBus eventBus;

    private final Clock clock;

    public BookingDto bookSeat(UUID seatId, String username) {
        var seatDetails = filmFacade.searchSeatDetails(seatId, clock);
        var bookingScreening = BookingScreening
                .builder()
                .id(seatDetails.screeningId())
                .timeToScreeningInHours(seatDetails.timeToScreeningInHours())
                .build();
        var seatBooking = BookingSeat
                .builder()
                .id(seatId)
                .isAvailable(seatDetails.isSeatAvailable())
                .screening(bookingScreening)
                .build();
        var booking = Booking.make(seatBooking, username);
        eventBus.post(
                new SeatBookedEvent(seatId)
        );
        return bookingRepository
                .save(booking)
                .toDto();
    }

    public void cancelSeat(UUID bookingId) {
        var booking = getBookingOrThrow(bookingId);
        booking.cancel();
        eventBus.post(
                new BookingCancelledEvent(booking.getSeat().getId())
        );
    }

    private Booking getBookingOrThrow(UUID bookingId) {
        return bookingRepository
                .findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);
    }
}
