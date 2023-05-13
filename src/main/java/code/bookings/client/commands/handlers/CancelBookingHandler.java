package code.bookings.client.commands.handlers;

import code.bookings.client.commands.CancelBookingCommand;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.exceptions.BookingNotFoundException;
import code.user.infrastrcuture.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Component
@RequiredArgsConstructor
public class CancelBookingHandler {

    private final SecurityHelper securityHelper;
    private final BookingRepository bookingRepository;
    private final Clock clock;

    @Transactional
    public void handle(CancelBookingCommand command) {
        var booking = getBookingOrThrow(command.bookingId());
        booking.cancel(clock);
    }

    private Booking getBookingOrThrow(Long bookingId) {
        var currentUser = securityHelper.getCurrentUser();
        return bookingRepository
                .readByIdAndUsername(bookingId, currentUser.getUsername())
                .orElseThrow(BookingNotFoundException::new);
    }
}
