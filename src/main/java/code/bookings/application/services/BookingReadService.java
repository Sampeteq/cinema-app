package code.bookings.application.services;

import code.bookings.application.dto.BookingDto;
import code.bookings.application.dto.BookingMapper;
import code.bookings.infrastructure.db.BookingRepository;
import code.shared.EntityNotFoundException;
import code.user.application.services.UserCurrentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingReadService {

    private final UserCurrentService userCurrentService;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public BookingDto read(Long id) {
        var currentUserId = userCurrentService.getCurrentUserId();
        return bookingRepository
                .readByIdAndUserId(id, currentUserId)
                .map(bookingMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
    }

    public List<BookingDto> readAll() {
        var currentUserId = userCurrentService.getCurrentUserId();
        return bookingRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(bookingMapper::mapToDto)
                .toList();
    }
}
