package code.bookings.application.services;

import code.bookings.domain.ports.BookingRepository;
import code.shared.exceptions.EntityNotFoundException;
import code.shared.time.TimeProvider;
import code.user.application.services.UserCurrentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingCancelService {

    private final UserCurrentService userCurrentService;
    private final BookingRepository bookingRepository;
    private final TimeProvider timeProvider;

    @Transactional
    public void cancelBooking(Long bookingId) {
        var currentUserId = userCurrentService.getCurrentUserId();
        var booking = bookingRepository
                .readByIdAndUserId(bookingId, currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
        booking.cancel(timeProvider.getCurrentDate());
    }
}
