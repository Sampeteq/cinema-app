package code.screenings.infrastructure;

import code.screenings.ScreeningFacade;
import code.screenings.dto.BookSeatDto;
import code.screenings.dto.SeatBookingDto;
import code.screenings.exception.BookingException;
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

    private final ScreeningFacade screeningFacade;

    private final Clock clock;

    @PostMapping("/seats-bookings")
    SeatBookingDto bookSeat(
            @RequestBody
            @Valid
            BookSeatDto request,
            Principal principal
    ) {
        return screeningFacade.bookSeat(request, principal.getName(), clock);
    }

    @PatchMapping("/seats-bookings/{bookingId}/cancel")
    void cancelSeatBooking(
            @PathVariable
            UUID bookingId
    ) {
        screeningFacade.cancelSeatBooking(bookingId, clock);
    }

    @GetMapping("/seats-bookings/my/{bookingId}")
    SeatBookingDto searchSeatBooking(@PathVariable UUID bookingId, Principal principal) {
        return screeningFacade.searchSeatBooking(bookingId, principal.getName());
    }

    @GetMapping("/seats-bookings/my")
    List<SeatBookingDto> searchSeatBookingsByUsername(Principal principal) {
        return screeningFacade.searchSeatBookingsByUsername(principal.getName());
    }
}

@RestControllerAdvice
class SeatBookingExceptionHandler {

    @ExceptionHandler(BookingException.class)
    ResponseEntity<?> handle(BookingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
