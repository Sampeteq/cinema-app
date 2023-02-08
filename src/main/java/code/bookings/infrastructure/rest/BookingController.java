package code.bookings.infrastructure.rest;

import code.bookings.application.BookingFacade;
import code.bookings.application.dto.BookingDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Clock;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingFacade bookingFacade;

    private final Clock clock;

    @PostMapping("/{seatId}")
    public BookingDto bookSeat(@PathVariable UUID seatId, Principal principal) {
        return bookingFacade.bookSeat(seatId, principal.getName(), clock);
    }

    @PatchMapping("/{bookingId}/cancel")
    public void cancelBooking(@PathVariable UUID bookingId) {
        bookingFacade.cancelBooking(bookingId, clock);
    }

    @GetMapping("/my")
    public List<BookingDto> searchAllBookings(Principal principal) {
        return bookingFacade.searchAllBookings(principal.getName());
    }

    @GetMapping("/my/{bookingId}")
    public BookingDto searchBookingById(@PathVariable UUID bookingId, Principal principal) {
        return bookingFacade.searchBookingById(bookingId, principal.getName());
    }
}

