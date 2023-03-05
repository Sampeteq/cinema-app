package code.bookings.infrastructure.rest;

import code.bookings.application.BookingFacade;
import code.bookings.application.dto.BookingDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingFacade bookingFacade;

    @PostMapping("/{seatId}")
    public BookingDto bookSeat(@PathVariable UUID seatId, Principal principal) {
        return bookingFacade.bookSeat(seatId, principal.getName());
    }

    @PostMapping("/{bookingId}/cancel")
    public void cancelBooking(@PathVariable UUID bookingId) {
        bookingFacade.cancelBooking(bookingId);
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

