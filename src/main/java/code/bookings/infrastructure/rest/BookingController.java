package code.bookings.infrastructure.rest;

import code.bookings.client.commands.CancelBookingCommand;
import code.bookings.client.commands.MakeBookingCommand;
import code.bookings.client.commands.handlers.CancelBookingHandler;
import code.bookings.client.commands.handlers.MakeBookingHandler;
import code.bookings.client.dto.BookingDto;
import code.bookings.client.queries.GetBookingQuery;
import code.bookings.client.queries.GetBookingsQuery;
import code.bookings.client.queries.handlers.GetBookingHandler;
import code.bookings.client.queries.handlers.GetBookingsHandler;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
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
    public BookingDto bookSeat(@RequestParam Long seatId, Principal principal) {
        var command = MakeBookingCommand
                .builder()
                .seatId(seatId)
                .userMail(principal.getName())
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
    public List<BookingDto> getAllBookings(Principal principal) {
        var getBookingsQuery = GetBookingsQuery
                .builder()
                .username(principal.getName())
                .build();
        return getBookingsHandler.handle(getBookingsQuery);
    }

    @GetMapping("/my/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId, Principal principal) {
        var getBookingQuery = GetBookingQuery
                .builder()
                .bookingId(bookingId)
                .username(principal.getName())
                .build();
        return getBookingHandler.handle(getBookingQuery);
    }
}

