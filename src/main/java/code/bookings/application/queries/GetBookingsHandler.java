package code.bookings.application.queries;

import code.bookings.application.dto.BookingDto;
import code.bookings.application.dto.BookingMapper;
import code.bookings.domain.BookingRepository;
import code.user.infrastrcuture.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetBookingsHandler {

    private final SecurityHelper securityHelper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public List<BookingDto> handle() {
        var currentUserId = securityHelper.getCurrentUserId();
        return bookingRepository
                .readByUserId(currentUserId)
                .stream()
                .map(bookingMapper::mapToDto)
                .toList();
    }
}
