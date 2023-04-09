package code.bookings.domain.queries.handlers;

import code.bookings.domain.queries.GetBookingsQuery;
import code.bookings.infrastructure.rest.dto.BookingDto;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetBookingsQueryHandler {

    private final BookingRepository bookingRepository;

    public List<BookingDto> handle(GetBookingsQuery query) {
        return bookingRepository
                .findByUsername(query.username())
                .stream()
                .map(Booking::toDto)
                .toList();
    }
}
