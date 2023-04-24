package code.bookings.client.queries.handlers;

import code.bookings.client.dto.BookingDto;
import code.bookings.client.dto.mappers.BookingMapper;
import code.bookings.client.queries.GetBookingQuery;
import code.bookings.domain.BookingRepository;
import code.bookings.infrastructure.exceptions.BookingNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetBookingHandler {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    public BookingDto handle(GetBookingQuery query) {
        return bookingRepository
                .getByIdAndUsername(query.bookingId(), query.username())
                .map(bookingMapper::mapToDto)
                .orElseThrow(BookingNotFoundException::new);
    }
}