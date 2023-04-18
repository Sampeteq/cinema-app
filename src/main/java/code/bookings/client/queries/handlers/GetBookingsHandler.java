package code.bookings.client.queries.handlers;

import code.bookings.client.dto.BookingDto;
import code.bookings.client.queries.GetBookingsQuery;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetBookingsHandler {

    private final BookingRepository bookingRepository;

    public List<BookingDto> handle(GetBookingsQuery query) {
        return bookingRepository
                .getByUsername(query.username())
                .stream()
                .map(Booking::toDto)
                .toList();
    }
}
