package code.bookings.application.commands;

import code.bookings.application.dto.BookingDto;
import code.bookings.application.dto.BookingMapper;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.screenings.domain.SeatReadOnlyRepository;
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
public class BookingMakingService {

    private final SeatReadOnlyRepository seatReadOnlyRepository;
    private final SecurityHelper securityHelper;
    private final Clock clock;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingDto makeBooking(BookingMakingCommand command) {
        log.info("Received a command:{}",command);
        var seat = seatReadOnlyRepository
                .getById(command.seatId())
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
        log.info("Found a seat for booking:{}",seat);
        var currentUserId = securityHelper.getCurrentUserId();
        var booking = Booking.make(seat, clock, currentUserId);
        var savedBooking = bookingRepository.add(booking);
        log.info("Saved a booking:{}", savedBooking);
        return bookingMapper.mapToDto(savedBooking);
    }
}
