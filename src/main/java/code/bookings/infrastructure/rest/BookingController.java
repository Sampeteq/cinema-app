package code.bookings.infrastructure.rest;

import code.bookings.application.commands.BookingCancellationCommand;
import code.bookings.application.dto.BookingId;
import code.bookings.application.handlers.BookingCancellationHandler;
import code.bookings.application.commands.BookingMakingCommand;
import code.bookings.application.handlers.BookingMakingHandler;
import code.bookings.application.dto.BookingDto;
import code.bookings.application.handlers.BookingReadingHandler;
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
public class BookingController {

    private final BookingMakingHandler bookingMakingHandler;

    private final BookingCancellationHandler bookingCancellationHandler;

    private final BookingReadingHandler bookingReadingHandler;

    @PostMapping
    public BookingId bookSeat(@RequestParam Long seatId) {
        var command = new BookingMakingCommand(seatId);
        return bookingMakingHandler.handle(command);
    }

    @PostMapping("/{bookingId}/cancel")
    public void cancelBooking(@PathVariable Long bookingId) {
        var command = new BookingCancellationCommand(bookingId);
        bookingCancellationHandler.handle(command);
    }

    @GetMapping("/my")
    public List<BookingDto> getAllBookings() {
        return bookingReadingHandler.readAll();
    }

    @GetMapping("/my/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId) {
        return bookingReadingHandler.handle(bookingId);
    }
}

