package code.bookings.domain.commands.handlers;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.BookingSeat;
import code.bookings.domain.commands.MakeBookingCommand;
import code.bookings.domain.events.DecreasedFreeSeatsEvent;
import code.bookings.infrastructure.rest.dto.BookingDto;
import code.films.domain.queries.GetSeatDetailsQuery;
import code.films.domain.queries.handlers.GetSeatDetailsHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MakeBookingHandler {

    private final GetSeatDetailsHandler getSeatDetailsHandler;

    private final BookingRepository bookingRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public BookingDto handle(MakeBookingCommand command) {
        var getSeatDetailsQuery = GetSeatDetailsQuery
                .builder()
                .seatid(command.seatId())
                .build();
        var seatDetails = getSeatDetailsHandler.handle(getSeatDetailsQuery);
        var bookingSeat = BookingSeat
                .builder()
                .id(command.seatId())
                .isAvailable(seatDetails.isAvailable())
                .timeToScreeningInHours(seatDetails.timeToScreeningInHour())
                .build();
        var booking = Booking.make(bookingSeat, command.username());
        var decreasedFreeSeatsEvent = DecreasedFreeSeatsEvent
                .builder()
                .seatId(command.seatId())
                .build();
        var bookingDto = bookingRepository
                .save(booking)
                .toDto();
        applicationEventPublisher.publishEvent(decreasedFreeSeatsEvent);
        return bookingDto;
    }
}
