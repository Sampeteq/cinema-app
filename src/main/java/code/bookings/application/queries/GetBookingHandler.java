package code.bookings.application.queries;

import code.bookings.application.dto.BookingDto;
import code.bookings.application.dto.BookingMapper;
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
        var currentUserId = securityHelper.getCurrentUserId();
        return bookingRepository
                .readByIdAndUserId(id, currentUserId)
                .map(bookingMapper::mapToDto)
                .orElseThrow(BookingNotFoundException::new);
    }
}
