package code.bookings.infrastructure.rest;

import code.bookings.application.commands.CancelBookingCommand;
import code.bookings.application.commands.CancelBookingHandler;
import code.bookings.application.commands.MakeBookingCommand;
import code.bookings.application.commands.MakeBookingHandler;
import code.bookings.application.dto.BookingDto;
import code.bookings.application.queries.GetBookingHandler;
import code.bookings.application.queries.GetBookingsHandler;
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

    private final MakeBookingHandler makeBookingHandler;

    private final CancelBookingHandler cancelBookingHandler;

    private final GetBookingsHandler getBookingsHandler;

    private final GetBookingHandler getBookingHandler;

    @PostMapping
    public BookingDto bookSeat(@RequestParam Long seatId) {
        var command = MakeBookingCommand
                .builder()
                .seatId(seatId)
                .build();
        return makeBookingHandler.handle(command);
    }

    @PostMapping("/{bookingId}/cancel")
    public void cancelBooking(@PathVariable Long bookingId) {
        var command = CancelBookingCommand
                .builder()
                .bookingId(bookingId)
                .build();
        cancelBookingHandler.handle(command);
    }

    @GetMapping("/my")
    public List<BookingDto> getAllBookings() {
        return getBookingsHandler.handle();
    }

    @GetMapping("/my/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId) {
        return getBookingHandler.handle(bookingId);
    }
}

