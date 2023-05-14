package code.bookings.client.queries.handlers;

import code.bookings.client.dto.BookingDto;
import code.bookings.client.dto.BookingMapper;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.exceptions.BookingNotFoundException;
import code.user.infrastrcuture.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetBookingHandler {

    private final SecurityHelper securityHelper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public BookingDto handle(Long id) {
        var currentUser = securityHelper.getCurrentUser();
        return bookingRepository
                .readByIdAndUserId(id, currentUser.getId())
                .map(bookingMapper::mapToDto)
                .orElseThrow(BookingNotFoundException::new);
    }
}
