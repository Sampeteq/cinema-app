package code.bookings.client.commands.handlers;

import code.bookings.client.commands.MakeBookingCommand;
import code.bookings.client.commands.events.DecreasedFreeSeatsEvent;
import code.bookings.client.dto.BookingDto;
import code.bookings.client.dto.mappers.BookingMapper;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.BookingSeat;
import code.films.client.queries.handlers.GetSeatDetailsHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MakeBookingHandler {

    private final GetSeatDetailsHandler getSeatDetailsHandler;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public BookingDto handle(MakeBookingCommand command) {
        var seatDetails = getSeatDetailsHandler.handle(command.seatId());
        var bookingSeat = BookingSeat
                .builder()
                .id(command.seatId())
                .isAvailable(seatDetails.isAvailable())
                .timeToScreeningInHours(seatDetails.timeToScreeningInHour())
                .build();
        var booking = Booking.make(bookingSeat, command.username());
        var savedBooking = bookingRepository.save(booking);
        var decreasedFreeSeatsEvent = DecreasedFreeSeatsEvent
                .builder()
                .seatId(command.seatId())
                .build();
        applicationEventPublisher.publishEvent(decreasedFreeSeatsEvent);
        var bookingDto = bookingMapper.mapToDto(savedBooking);
        return bookingDto;
    }
}
