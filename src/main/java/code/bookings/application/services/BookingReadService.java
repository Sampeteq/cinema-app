package code.bookings.application.services;

import code.bookings.application.dto.BookingViewDto;
import code.bookings.application.dto.BookingViewMapper;
import code.bookings.domain.ports.BookingViewRepository;
import code.shared.exceptions.EntityNotFoundException;
import code.user.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class BookingReadService {

    private final UserFacade userFacade;
    private final BookingViewMapper bookingViewMapper;
    private final BookingViewRepository bookingViewRepository;

    BookingViewDto readById(Long id) {
        var currentUserId = userFacade.readCurrentUserId();
        return bookingViewRepository
                .readByIdAndUserId(id, currentUserId)
                .map(bookingViewMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
    }

    List<BookingViewDto> readAll() {
        var currentUserId = userFacade.readCurrentUserId();
        return bookingViewRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(bookingViewMapper::mapToDto)
                .toList();
    }
}
