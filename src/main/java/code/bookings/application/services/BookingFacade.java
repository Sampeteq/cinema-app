package code.bookings.application.services;

import code.bookings.application.dto.BookingMakeDto;
import code.bookings.application.dto.BookingViewDto;
import code.catalog.application.dto.SeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingFacade {

    private final BookingMakeService bookingMakeService;
    private final BookingCancelService bookingCancelService;
    private final BookingReadService bookingReadService;
    private final SeatReadService seatReadService;

    public void makeBooking(BookingMakeDto dto) {
        bookingMakeService.makeBooking(dto);
    }

    public void cancelBooking(Long bookingId) {
        bookingCancelService.cancelBooking(bookingId);
    }

    public BookingViewDto readBookingById(Long id) {
        return bookingReadService.readById(id);
    }

    public List<BookingViewDto> readAllBookings() {
        return bookingReadService.readAll();
    }

    public List<SeatDto> readSeatsByScreeningId(Long screeningId) {
        return seatReadService.readSeatsByScreeningId(screeningId);
    }
}
