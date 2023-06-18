package code.bookings.infrastructure.rest;

import code.bookings.application.dto.BookingDto;
import code.bookings.application.dto.BookingId;
import code.bookings.application.services.BookingCancelService;
import code.bookings.application.services.BookingMakeService;
import code.bookings.application.services.BookingReadService;
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
public class BookingRestController {

    private final BookingMakeService bookingMakeHandler;

    private final BookingCancelService bookingCancelHandler;

    private final BookingReadService bookingReadHandler;

    @PostMapping
    public BookingId bookSeat(@RequestParam Long seatId) {
        return bookingMakeHandler.makeBooking(seatId);
    }

    @PostMapping("/{bookingId}/cancel")
    public void cancelBooking(@PathVariable Long bookingId) {
        bookingCancelHandler.cancelBooking(bookingId);
    }

    @GetMapping("/my")
    public List<BookingDto> getAllBookings() {
        return bookingReadHandler.readAll();
    }

    @GetMapping("/my/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId) {
        return bookingReadHandler.read(bookingId);
    }
}

