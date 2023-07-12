package code.bookings.application.services;

import code.bookings.domain.events.BookingCancelledEvent;
import code.bookings.domain.ports.BookingRepository;
import code.shared.EntityNotFoundException;
import code.user.application.services.UserCurrentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class BookingCancelService {

    private final UserCurrentService userCurrentService;
    private final BookingRepository bookingRepository;
    private final Clock clock;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void cancelBooking(Long bookingId) {
        var currentUserId = userCurrentService.getCurrentUserId();
        var booking = bookingRepository
                .readByIdAndUserId(bookingId, currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
        booking.cancel(clock);
        var seatBookingCancelledEvent = new BookingCancelledEvent(booking.getSeat().getId());
        applicationEventPublisher.publishEvent(seatBookingCancelledEvent);
    }
}
