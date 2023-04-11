package code.bookings.domain.commands.handlers;

import code.bookings.infrastructure.rest.dto.BookingDto;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.Seat;
import code.bookings.domain.SeatRepository;
import code.bookings.domain.commands.BookingMakeCommand;
import code.bookings.infrastructure.exceptions.SeatNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingMakeCommandHandler {

    private final SeatRepository seatRepository;

    private final BookingRepository bookingRepository;

    private final Clock clock;

    @Transactional
    public BookingDto handle(BookingMakeCommand command) {
        var seat = getSeatOrThrow(command.seatId());
        var booking = Booking.make(seat, command.username(), clock);
        return bookingRepository
                .save(booking)
                .toDto();
    }

    private Seat getSeatOrThrow(UUID seatId) {
        return seatRepository
                .findById(seatId)
                .orElseThrow(SeatNotFoundException::new);
    }
}
