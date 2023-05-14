package code.bookings.application.queries.handlers;

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
        var currentUser = securityHelper.getCurrentUser();
        return bookingRepository
                .readByUserId(currentUser.getId())
                .stream()
                .map(bookingMapper::mapToDto)
                .toList();
    }
}
