package code.bookings.client.commands.handlers;

import code.bookings.client.commands.CancelBookingCommand;
import code.bookings.client.exceptions.BookingNotFoundException;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CancelBookingHandler {

    private final BookingRepository bookingRepository;
    private final Clock clock;

    @Transactional
    public void handle(CancelBookingCommand command) {
        var booking = getBookingOrThrow(command.bookingId());
        booking.cancel(clock);
    }

    private Booking getBookingOrThrow(UUID bookingId) {
        return bookingRepository
                .readById(bookingId)
                .orElseThrow(BookingNotFoundException::new);
    }
}
