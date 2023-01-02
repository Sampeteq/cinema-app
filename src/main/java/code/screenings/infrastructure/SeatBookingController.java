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
import java.time.Clock;
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
            BookSeatDto dto
    ) {
        return screeningFacade.bookSeat(dto, clock);
    }

    @PatchMapping("/seats-bookings/{bookingId}/cancel")
    void cancelSeatBooking(
            @PathVariable
            UUID bookingId
    ) {
        screeningFacade.cancelSeatBooking(bookingId, clock);
    }

    @GetMapping("/seats-bookings/{bookingId}")
    SeatBookingDto searchSeatBooking(@PathVariable UUID bookingId) {
        return screeningFacade.searchSeatBooking(bookingId);
    }
}

@RestControllerAdvice
class SeatBookingExceptionHandler {

    @ExceptionHandler(BookingException.class)
    ResponseEntity<?> handle(BookingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
