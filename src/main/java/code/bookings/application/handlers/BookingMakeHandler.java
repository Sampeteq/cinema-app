package code.bookings.application.handlers;

import code.bookings.application.commands.BookingMakeCommand;
import code.bookings.application.dto.BookingId;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.exceptions.BookingAlreadyExists;
import code.films.domain.FilmScreeningSeatReadOnlyRepository;
import code.shared.EntityNotFoundException;
import code.user.infrastrcuture.SecurityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingMakeHandler {

    private final BookingRepository bookingRepository;
    private final FilmScreeningSeatReadOnlyRepository seatReadOnlyRepository;
    private final SecurityHelper securityHelper;
    private final Clock clock;

    @Transactional
    public BookingId handle(BookingMakeCommand command) {
        log.info("Received a command:{}",command);
        if (bookingRepository.existsBySeatId(command.seatId())) {
            log.error("Booking already exists");
            throw new BookingAlreadyExists();
        }
        var seat = seatReadOnlyRepository
                .getById(command.seatId())
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
        log.info("Found a seat for booking:{}",seat);
        var currentUserId = securityHelper.getCurrentUserId();
        var booking = Booking.make(seat, clock, currentUserId);
        var addedBooking = bookingRepository.add(booking);
        log.info("Added a booking:{}", addedBooking);
        seat.makeNotFree();
        return new BookingId(addedBooking.getId());
    }
}
