package code.bookings.infrastructure.rest;

import code.bookings.infrastructure.rest.dto.BookingDto;
import code.bookings.domain.commands.BookingCancelCommand;
import code.bookings.domain.commands.BookingMakeCommand;
import code.bookings.domain.commands.handlers.BookingCancelCommandHandler;
import code.bookings.domain.commands.handlers.BookingMakeCommandHandler;
import code.bookings.domain.queries.GetBookingQuery;
import code.bookings.domain.queries.GetBookingsQuery;
import code.bookings.domain.queries.handlers.GetBookingQueryHandler;
import code.bookings.domain.queries.handlers.GetBookingsQueryHandler;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingMakeCommandHandler bookingMakeCommandHandler;

    private final BookingCancelCommandHandler bookingCancelCommandHandler;

    private final GetBookingsQueryHandler getBookingsQueryHandler;

    private final GetBookingQueryHandler getBookingQueryHandler;

    @PostMapping
    public BookingDto bookSeat(@RequestParam UUID seatId, Principal principal) {
        var command = BookingMakeCommand
                .builder()
                .seatId(seatId)
                .username(principal.getName())
                .build();
        return bookingMakeCommandHandler.handle(command);
    }

    @PostMapping("/{bookingId}/cancel")
    public void cancelBooking(@PathVariable UUID bookingId) {
        var command = BookingCancelCommand
                .builder()
                .bookingId(bookingId)
                .build();
        bookingCancelCommandHandler.handle(command);
    }

    @GetMapping("/my")
    public List<BookingDto> getAllBookings(Principal principal) {
        var getBookingsQuery = GetBookingsQuery
                .builder()
                .username(principal.getName())
                .build();
        return getBookingsQueryHandler.handle(getBookingsQuery);
    }

    @GetMapping("/my/{bookingId}")
    public BookingDto getBookingById(@PathVariable UUID bookingId, Principal principal) {
        var getBookingQuery = GetBookingQuery
                .builder()
                .bookingId(bookingId)
                .username(principal.getName())
                .build();
        return getBookingQueryHandler.handle(getBookingQuery);
    }
}

