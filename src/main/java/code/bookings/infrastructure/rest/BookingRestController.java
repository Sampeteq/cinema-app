package code.bookings.infrastructure.rest;

import code.bookings.application.dto.BookingViewDto;
import code.bookings.application.services.BookingFacade;
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

    private final BookingFacade bookingFacade;

    @PostMapping
    void bookSeat(@RequestParam Long screeningId, @RequestParam Long seatId) {
        bookingFacade.makeBooking(screeningId, seatId);
    }

    @PostMapping("/{bookingId}/cancel")
    void cancelBooking(@PathVariable Long bookingId) {
        bookingFacade.cancelBooking(bookingId);
    }

    @GetMapping("/my")
    List<BookingViewDto> readAllBookings() {
        return bookingFacade.readAllBookings();
    }

    @GetMapping("/my/{bookingId}")
    BookingViewDto readBookingById(@PathVariable Long bookingId) {
        return bookingFacade.readBookingById(bookingId);
    }

    @GetMapping("/seats")
    List<SeatDto> readSeats(@RequestParam Long screeningId) {
        return bookingFacade.readSeatsByScreeningId(screeningId);
    }
}

