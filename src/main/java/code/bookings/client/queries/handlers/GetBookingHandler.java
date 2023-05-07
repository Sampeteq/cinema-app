package code.bookings.client.queries.handlers;

import code.bookings.client.dto.BookingDto;
import code.bookings.client.dto.BookingMapper;
import code.bookings.client.exceptions.BookingNotFoundException;
import code.bookings.client.queries.GetBookingQuery;
import code.bookings.domain.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetBookingHandler {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public BookingDto handle(GetBookingQuery query) {
        return bookingRepository
                .readByIdAndUsername(query.bookingId(), query.username())
                .map(bookingMapper::mapToDto)
                .orElseThrow(BookingNotFoundException::new);
    }
}
