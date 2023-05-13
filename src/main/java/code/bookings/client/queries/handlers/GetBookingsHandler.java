package code.bookings.client.queries.handlers;

import code.bookings.client.dto.BookingDto;
import code.bookings.client.dto.BookingMapper;
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
                .readByUsername(currentUser.getUsername())
                .stream()
                .map(bookingMapper::mapToDto)
                .toList();
    }
}
