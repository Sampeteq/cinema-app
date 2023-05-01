package code.bookings.client.commands.handlers;

import code.bookings.client.commands.MakeBookingCommand;
import code.bookings.client.dto.BookingDto;
import code.bookings.client.dto.mappers.BookingMapper;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.exceptions.SeatNotAvailableException;
import code.seats.domain.SeatReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Component
@RequiredArgsConstructor
@Slf4j
public class MakeBookingHandler {

    private final SeatReadOnlyRepository seatReadOnlyRepository;
    private final Clock clock;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingDto handle(MakeBookingCommand command) {
        log.info("Received a command:{}",command);
        var seat = seatReadOnlyRepository
                .getById(command.seatId())
                .orElseThrow(SeatNotAvailableException::new);
        log.info("Found a seat for booking:{}",seat);
        var booking = Booking.make(seat, command.username(), clock);
        var savedBooking = bookingRepository.add(booking);
        log.info("Saved a booking:{}", savedBooking);
        return bookingMapper.mapToDto(savedBooking);
    }
}
