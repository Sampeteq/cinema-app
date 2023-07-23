package code.bookings.application.services;

import code.bookings.application.dto.BookingDetailsDto;
import code.bookings.application.dto.BookingDetailsMapper;
import code.bookings.domain.ports.BookingRepository;
import code.shared.EntityNotFoundException;
import code.user.application.services.UserCurrentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingReadService {

    private final UserCurrentService userCurrentService;
    private final BookingDetailsMapper bookingDetailsMapper;
    private final BookingRepository bookingRepository;

    public BookingDetailsDto read(Long id) {
        var currentUserId = userCurrentService.getCurrentUserId();
        return bookingRepository
                .readByIdAndUserId(id, currentUserId)
                .map(bookingDetailsMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
    }

    public List<BookingDetailsDto> readAll() {
        var currentUserId = userCurrentService.getCurrentUserId();
        return bookingRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(bookingDetailsMapper::mapToDto)
                .toList();
    }
}
