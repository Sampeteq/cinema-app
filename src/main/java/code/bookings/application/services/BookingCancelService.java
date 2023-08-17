package code.bookings.application.services;

import code.bookings.domain.events.BookingCancelledEvent;
import code.bookings.domain.ports.BookingRepository;
import code.shared.events.EventPublisher;
import code.shared.exceptions.EntityNotFoundException;
import code.shared.time.TimeProvider;
import code.user.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class BookingCancelService {

    private final UserFacade userFacade;
    private final BookingRepository bookingRepository;
    private final TimeProvider timeProvider;
    private final EventPublisher eventPublisher;

    @Transactional
    public void cancelBooking(Long bookingId) {
        var currentUserId = userFacade.readCurrentUserId();
        var booking = bookingRepository
                .readByIdAndUserId(bookingId, currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
        booking.cancel(timeProvider.getCurrentDate());
        var bookingCancelledEvent = new BookingCancelledEvent(bookingId);
        eventPublisher.publish(bookingCancelledEvent);
    }
}
