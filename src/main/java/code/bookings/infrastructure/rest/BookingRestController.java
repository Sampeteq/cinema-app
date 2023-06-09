package code.bookings.infrastructure.rest;

import code.bookings.application.commands.BookingCancelCommand;
import code.bookings.application.dto.BookingId;
import code.bookings.application.handlers.BookingCancelHandler;
import code.bookings.application.commands.BookingMakeCommand;
import code.bookings.application.handlers.BookingMakeHandler;
import code.bookings.application.dto.BookingDto;
import code.bookings.application.handlers.BookingReadHandler;
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

    private final BookingMakeHandler bookingMakeHandler;

    private final BookingCancelHandler bookingCancelHandler;

    private final BookingReadHandler bookingReadHandler;

    @PostMapping
    public BookingId bookSeat(@RequestParam Long seatId) {
        var command = new BookingMakeCommand(seatId);
        return bookingMakeHandler.handle(command);
    }

    @PostMapping("/{bookingId}/cancel")
    public void cancelBooking(@PathVariable Long bookingId) {
        var command = new BookingCancelCommand(bookingId);
        bookingCancelHandler.handle(command);
    }

    @GetMapping("/my")
    public List<BookingDto> getAllBookings() {
        return bookingReadHandler.readAll();
    }

    @GetMapping("/my/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId) {
        return bookingReadHandler.handle(bookingId);
    }
}

