package code.bookings.domain.queries.handlers;

import code.bookings.domain.queries.GetBookingQuery;
import code.bookings.infrastructure.rest.dto.BookingDto;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.infrastructure.exceptions.BookingNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetBookingQueryHandler {

    private final BookingRepository bookingRepository;

    public BookingDto handle(GetBookingQuery query) {
        return getBookingOrThrow(query.bookingId(), query.username()).toDto();
    }

    private Booking getBookingOrThrow(UUID ticketId, String username) {
        return bookingRepository
                .getByIdAndUsername(ticketId, username)
                .orElseThrow(BookingNotFoundException::new);
    }
}
