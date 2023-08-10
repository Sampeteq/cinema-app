package code.bookings.application.services;

import code.bookings.domain.Booking;
import code.bookings.domain.events.BookingMadeEvent;
import code.bookings.domain.ports.BookingRepository;
import code.bookings.domain.ports.SeatRepository;
import code.shared.events.EventPublisher;
import code.shared.exceptions.EntityNotFoundException;
import code.shared.time.TimeProvider;
import code.user.application.services.UserCurrentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingMakeService {

    private final SeatRepository seatRepository;
    private final UserCurrentService userCurrentService;
    private final TimeProvider timeProvider;
    private final BookingRepository bookingRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public void makeBooking(Long seatId) {
        var seat = seatRepository
                .readById(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
        var currentUserId = userCurrentService.getCurrentUserId();
        var booking = Booking.make(seat, timeProvider.getCurrentDate(), currentUserId);
        var addedBooking = bookingRepository.add(booking);
        log.info("Added a booking:{}", addedBooking);
        var screening = seat.getScreening();
        var bookingMadeEvent = new BookingMadeEvent(
                addedBooking.getId(),
                screening.getId(),
                screening.getDate(),
                seat.getRowNumber(),
                seat.getRowNumber(),
                currentUserId
        );
        eventPublisher.publish(bookingMadeEvent);
        log.info("Event published: {}", bookingMadeEvent);
    }
}
