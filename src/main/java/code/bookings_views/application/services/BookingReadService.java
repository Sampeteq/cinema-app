package code.bookings_views.application.services;

import code.bookings_views.application.dto.BookingViewDto;
import code.bookings_views.application.dto.BookingViewMapper;
import code.bookings_views.domain.ports.BookingViewRepository;
import code.shared.exceptions.EntityNotFoundException;
import code.user.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingReadService {

    private final UserFacade userFacade;
    private final BookingViewMapper bookingViewMapper;
    private final BookingViewRepository bookingViewRepository;

    public BookingViewDto readById(Long id) {
        var currentUserId = userFacade.readCurrentUserId();
        return bookingViewRepository
                .readByIdAndUserId(id, currentUserId)
                .map(bookingViewMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Booking"));
    }

    public List<BookingViewDto> readAll() {
        var currentUserId = userFacade.readCurrentUserId();
        return bookingViewRepository
                .readAllByUserId(currentUserId)
                .stream()
                .map(bookingViewMapper::mapToDto)
                .toList();
    }
}
