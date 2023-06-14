package code.bookings.application.services;

import code.bookings.application.commands.BookingCancelCommand;
import code.bookings.infrastructure.db.BookingRepository;
import code.shared.EntityNotFoundException;
import code.user.infrastrcuture.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Component
@RequiredArgsConstructor
public class BookingCancelService {

    private final SecurityHelper securityHelper;
    private final BookingRepository bookingRepository;
    private final Clock clock;

    @Transactional
    public void cancelBooking(BookingCancelCommand command) {
        var currentUserId = securityHelper.getCurrentUserId();
        bookingRepository
                .readByIdAndUserId(command.bookingId(), currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Booking"))
                .cancel(clock);
    }
}
