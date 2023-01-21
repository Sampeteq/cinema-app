package code.bookings.infrastructure;

import code.bookings.BookingFacade;
import code.bookings.dto.BookDto;
import code.bookings.dto.BookingDto;
import code.bookings.exception.BookingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.Clock;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
class SeatBookingController {

    private final BookingFacade bookingFacade;

    private final Clock clock;

    @PostMapping("/seats-bookings")
    BookingDto bookSeat(
            @RequestBody
            @Valid
            BookDto request,
            Principal principal
    ) {
        return bookingFacade.bookSeat(request, principal.getName(), clock);
    }

    @PatchMapping("/seats-bookings/{bookingId}/cancel")
    void cancelSeatBooking(
            @PathVariable
            UUID bookingId
    ) {
        bookingFacade.cancelSeatBooking(bookingId, clock);
    }

    @GetMapping("/seats-bookings/my/{bookingId}")
    BookingDto searchSeatBooking(@PathVariable UUID bookingId, Principal principal) {
        return bookingFacade.searchSeatBooking(bookingId, principal.getName());
    }

    @GetMapping("/seats-bookings/my")
    List<BookingDto> searchSeatBookingsByUsername(Principal principal) {
        return bookingFacade.searchSeatBookingsByUsername(principal.getName());
    }
}

@RestControllerAdvice
class SeatBookingExceptionHandler {

    @ExceptionHandler(BookingException.class)
    ResponseEntity<?> handle(BookingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
