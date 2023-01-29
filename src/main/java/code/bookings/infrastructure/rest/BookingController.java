package code.bookings.infrastructure.rest;

import code.bookings.application.BookingFacade;
import code.bookings.application.dto.BookDto;
import code.bookings.application.dto.BookingDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping
    public BookingDto bookSeat(@RequestBody @Valid BookDto dto, Principal principal) {
        return bookingFacade.bookSeat(dto, principal.getName(), clock);
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

