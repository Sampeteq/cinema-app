package code.bookings.infrastructure.rest;

import code.bookings.application.dto.BookingViewDto;
import code.bookings.application.services.BookingCancelService;
import code.bookings.application.services.BookingMakeService;
import code.bookings.application.services.BookingReadService;
import code.bookings.application.services.SeatReadService;
import code.catalog.application.dto.SeatDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
class BookingRestController {

    private final BookingMakeService bookingMakeHandler;

    private final BookingCancelService bookingCancelHandler;

    private final BookingReadService bookingReadService;

    private final SeatReadService seatReadService;

    @PostMapping
    void bookSeat(@RequestParam Long screeningId, @RequestParam Long seatId) {
        bookingMakeHandler.makeBooking(screeningId, seatId);
    }

    @PostMapping("/{bookingId}/cancel")
    void cancelBooking(@PathVariable Long bookingId) {
        bookingCancelHandler.cancelBooking(bookingId);
    }

    @GetMapping("/my")
    List<BookingViewDto> readAllBookings() {
        return bookingReadService.readAll();
    }

    @GetMapping("/my/{bookingId}")
    BookingViewDto readBookingById(@PathVariable Long bookingId) {
        return bookingReadService.read(bookingId);
    }

    @GetMapping("/seats")
    List<SeatDto> readSeats(@RequestParam Long screeningId) {
        return seatReadService.readSeatsByScreeningId(screeningId);
    }
}

