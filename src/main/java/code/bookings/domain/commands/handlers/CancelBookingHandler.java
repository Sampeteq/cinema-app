package code.bookings.domain.commands.handlers;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.commands.CancelBookingCommand;
import code.bookings.domain.events.IncreasedFreeSeatsEvent;
import code.bookings.infrastructure.exceptions.BookingNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CancelBookingHandler {

    private final BookingRepository bookingRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void handle(CancelBookingCommand command) {
        var booking = getBookingOrThrow(command.bookingId());
        booking.cancel();
        var increasedFreeSeatsEvent = IncreasedFreeSeatsEvent
                .builder()
                .seatId(booking.getSeat().getId())
                .build();
        applicationEventPublisher.publishEvent(increasedFreeSeatsEvent);
    }

    private Booking getBookingOrThrow(UUID bookingId) {
        return bookingRepository
                .findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);
    }
}
