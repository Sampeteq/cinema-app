package code.bookings.infrastructure.rest;

import code.bookings.application.commands.BookingCancellationCommand;
import code.bookings.application.services.BookingCancellationService;
import code.bookings.application.commands.BookingMakingCommand;
import code.bookings.application.services.BookingMakingService;
import code.bookings.application.dto.BookingDto;
import code.bookings.application.services.BookingReadingService;
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

    private final BookingMakingService bookingMakingService;

    private final BookingCancellationService bookingCancellationService;

    private final BookingReadingService bookingReadingService;

    @PostMapping
    public BookingDto bookSeat(@RequestParam Long seatId) {
        var command = BookingMakingCommand
                .builder()
                .seatId(seatId)
                .build();
        return bookingMakingService.makeBooking(command);
    }

    @PostMapping("/{bookingId}/cancel")
    public void cancelBooking(@PathVariable Long bookingId) {
        var command = BookingCancellationCommand
                .builder()
                .bookingId(bookingId)
                .build();
        bookingCancellationService.cancelBooking(command);
    }

    @GetMapping("/my")
    public List<BookingDto> getAllBookings() {
        return bookingReadingService.readAll();
    }

    @GetMapping("/my/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId) {
        return bookingReadingService.read(bookingId);
    }
}

