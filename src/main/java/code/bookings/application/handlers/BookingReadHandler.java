package code.bookings.application.handlers;

import code.bookings.application.dto.BookingDto;
import code.bookings.application.dto.BookingMapper;
import code.bookings.domain.BookingRepository;
import code.shared.EntityNotFoundException;
import code.user.infrastrcuture.SecurityHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingReadHandler {

    private final SecurityHelper securityHelper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public BookingDto handle(Long id) {
        var currentUserId = securityHelper.getCurrentUserId();
        return bookingRepository
                .readByIdAndUserId(id, currentUserId)
                .map(bookingMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
    }

    public List<BookingDto> readAll() {
        var currentUserId = securityHelper.getCurrentUserId();
        return bookingRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(bookingMapper::mapToDto)
                .toList();
    }
}
