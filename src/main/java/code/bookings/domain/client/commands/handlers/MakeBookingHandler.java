package code.bookings.domain.client.commands.handlers;

import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.BookingSeat;
import code.bookings.domain.client.commands.MakeBookingCommand;
import code.bookings.domain.client.commands.events.DecreasedFreeSeatsEvent;
import code.bookings.domain.client.dto.BookingDto;
import code.films.domain.client.queries.GetSeatDetailsQuery;
import code.films.domain.client.queries.handlers.GetSeatDetailsHandler;
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
        applicationEventPublisher.publishEvent(decreasedFreeSeatsEvent);
        return bookingRepository
                .save(booking)
                .toDto();
    }
}
