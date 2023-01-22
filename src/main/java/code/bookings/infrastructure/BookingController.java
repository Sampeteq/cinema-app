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
@RequestMapping("/bookings")
@AllArgsConstructor
class BookingController {

    private final BookingFacade bookingFacade;

    private final Clock clock;

    @PostMapping
    BookingDto bookSeat(
            @RequestBody
            @Valid
            BookDto dto,
            Principal principal
    ) {
        return bookingFacade.bookSeat(dto, principal.getName(), clock);
    }

    @PatchMapping("/{bookingId}/cancel")
    void cancelBooking(
            @PathVariable
            UUID bookingId
    ) {
        bookingFacade.cancelBooking(bookingId, clock);
    }

    @GetMapping("/my")
    List<BookingDto> searchAllBookings(Principal principal) {
        return bookingFacade.searchAllBookings(principal.getName());
    }

    @GetMapping("/my/{bookingId}")
    BookingDto searchBookingById(@PathVariable UUID bookingId, Principal principal) {
        return bookingFacade.searchBookingById(bookingId, principal.getName());
    }
}

@RestControllerAdvice
class SeatBookingExceptionHandler {

    @ExceptionHandler(BookingException.class)
    ResponseEntity<?> handle(BookingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
