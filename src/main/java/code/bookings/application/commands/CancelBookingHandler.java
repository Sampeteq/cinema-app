package code.bookings.application.commands;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.shared.EntityNotFoundException;
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
        var currentUserId = securityHelper.getCurrentUserId();
        return bookingRepository
                .readByIdAndUserId(bookingId, currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
    }
}
