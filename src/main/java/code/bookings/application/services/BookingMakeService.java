package code.bookings.application.services;

import code.bookings.application.dto.BookingId;
import code.bookings.domain.Booking;
import code.bookings.domain.exceptions.BookingAlreadyExists;
import code.bookings.domain.ports.BookingRepository;
import code.catalog.infrastructure.db.SeatReadOnlyRepository;
import code.shared.EntityNotFoundException;
import code.user.application.services.UserCurrentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingMakeService {

    private final BookingRepository bookingRepository;
    private final SeatReadOnlyRepository seatReadOnlyRepository;
    private final UserCurrentService userCurrentService;
    private final Clock clock;

    @Transactional
    public BookingId makeBooking(Long seatId) {
        if (bookingRepository.existsBySeatId(seatId)) {
            log.error("Booking already exists");
            throw new BookingAlreadyExists();
        }
        var seat = seatReadOnlyRepository
                .getById(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Seat"));
        log.info("Found a seat for booking:{}",seat);
        var currentUserId = userCurrentService.getCurrentUserId();
        var booking = Booking.make(seat, clock, currentUserId);
        var addedBooking = bookingRepository.add(booking);
        log.info("Added a booking:{}", addedBooking);
        seat.makeNotFree();
        return new BookingId(addedBooking.getId());
    }
}
