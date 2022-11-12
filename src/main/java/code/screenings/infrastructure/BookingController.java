package code.screenings.infrastructure;

import code.screenings.ScreeningFacade;
import code.screenings.dto.BookScreeningTicketDTO;
import code.screenings.dto.TicketDTO;
import code.screenings.exception.BookingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
class BookingController {

    private final ScreeningFacade screeningFacade;

    private final Clock clock = Clock.systemUTC();

    @PostMapping("/screenings-tickets")
    TicketDTO bookTicket(
            @RequestBody
            @Valid
            BookScreeningTicketDTO dto
    ) {
        return screeningFacade.bookTicket(dto, clock);
    }

    @PatchMapping("/screenings-tickets/{ticketUUID}/cancel")
    void cancelTicket(
            @PathVariable
            UUID ticketUUID
    ) {
        screeningFacade.cancelTicket(ticketUUID, clock);
    }

    @GetMapping("/screenings-tickets")
    List<TicketDTO> readAllTickets() {
        return screeningFacade.readAllTickets();
    }
}

@RestControllerAdvice
class BookingExceptionHandler {

    @ExceptionHandler(BookingException.class)
    ResponseEntity<?> handle(BookingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
