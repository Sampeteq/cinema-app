package code.bookings.application.services;

import code.bookings.application.dto.BookingDetailsDto;
import code.bookings.application.dto.BookingDetailsMapper;
import code.bookings.infrastructure.db.BookingDetailsRepository;
import code.shared.EntityNotFoundException;
import code.user.application.services.UserCurrentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingReadService {

    private final UserCurrentService userCurrentService;
    private final BookingDetailsRepository bookingDetailsRepository;
    private final BookingDetailsMapper bookingDetailsMapper;

    public BookingDetailsDto read(Long id) {
        var currentUserId = userCurrentService.getCurrentUserId();
        return bookingDetailsRepository
                .findByBookingIdAndBookingUserId(id, currentUserId)
                .map(bookingDetailsMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
    }

    public List<BookingDetailsDto> readAll() {
        var currentUserId = userCurrentService.getCurrentUserId();
        return bookingDetailsRepository
                .findAllByBookingUserId(currentUserId)
                .stream()
                .map(bookingDetailsMapper::mapToDto)
                .toList();
    }
}
