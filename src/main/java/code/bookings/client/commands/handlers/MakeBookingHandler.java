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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MakeBookingHandler {

    private final GetSeatDetailsHandler getSeatDetailsHandler;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public BookingDto handle(MakeBookingCommand command) {
        log.info("Received a command:{}",command);
        var seatDetails = getSeatDetailsHandler.handle(command.seatId());
        var bookingSeat = BookingSeat
                .builder()
                .id(command.seatId())
                .isAvailable(seatDetails.isAvailable())
                .timeToScreeningInHours(seatDetails.timeToScreeningInHour())
                .build();
        log.info("Found a seat for booking:{}",bookingSeat);
        var booking = Booking.make(bookingSeat, command.username());
        var savedBooking = bookingRepository.save(booking);
        log.info("Saved a booking:{}", savedBooking);
        var decreasedFreeSeatsEvent = DecreasedFreeSeatsEvent
                .builder()
                .seatId(command.seatId())
                .build();
        applicationEventPublisher.publishEvent(decreasedFreeSeatsEvent);
        log.info("Published an event:{}",decreasedFreeSeatsEvent);
        var bookingDto = bookingMapper.mapToDto(savedBooking);
        return bookingDto;
    }
}
