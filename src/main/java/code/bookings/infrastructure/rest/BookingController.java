package code.bookings.infrastructure.rest;

import code.bookings.application.dto.BookingDto;
import code.bookings.application.services.BookingSearchService;
import code.bookings.application.services.BookingService;
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

    private final BookingService bookingService;

    private final BookingSearchService bookingSearchService;

    @PostMapping
    public BookingDto bookSeat(@RequestParam UUID seatId, Principal principal) {
        return bookingService.make(seatId, principal.getName());
    }

    @PostMapping("/{bookingId}/cancel")
    public void cancelBooking(@PathVariable UUID bookingId) {
        bookingService.cancel(bookingId);
    }

    @GetMapping("/my")
    public List<BookingDto> searchAllBookings(Principal principal) {
        return bookingSearchService.searchAllBookings(principal.getName());
    }

    @GetMapping("/my/{bookingId}")
    public BookingDto searchBookingById(@PathVariable UUID bookingId, Principal principal) {
        return bookingSearchService.searchBookingById(bookingId, principal.getName());
    }
}

