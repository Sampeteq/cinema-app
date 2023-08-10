package code.bookings.application.services;

import code.bookings.application.dto.BookingViewDto;
import code.bookings.application.dto.BookingViewMapper;
import code.bookings.domain.ports.BookingViewRepository;
import code.shared.exceptions.EntityNotFoundException;
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
    private final BookingViewMapper bookingViewMapper;
    private final BookingViewRepository bookingViewRepository;

    public BookingViewDto read(Long id) {
        var currentUserId = userCurrentService.getCurrentUserId();
        return bookingViewRepository
                .readByIdAndUserId(id, currentUserId)
                .map(bookingViewMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
    }

    public List<BookingViewDto> readAll() {
        var currentUserId = userCurrentService.getCurrentUserId();
        return bookingViewRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(bookingViewMapper::mapToDto)
                .toList();
    }
}
