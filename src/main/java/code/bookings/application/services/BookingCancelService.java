package code.bookings.application.services;

import code.bookings.domain.ports.BookingRepository;
import code.shared.EntityNotFoundException;
import code.user.application.services.UserCurrentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class BookingCancelService {

    private final UserCurrentService userCurrentService;
    private final BookingRepository bookingRepository;
    private final Clock clock;

    @Transactional
    public void cancelBooking(Long bookingId) {
        var currentUserId = userCurrentService.getCurrentUserId();
        bookingRepository
                .readByIdAndUserId(bookingId, currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Booking"))
                .cancel(clock);
    }
}
