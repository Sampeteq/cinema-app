package code.reservations.infrastructure;

import code.reservations.ReservationFacade;
import code.reservations.dto.ReserveScreeningTicketDTO;
import code.reservations.dto.TicketDTO;
import code.reservations.exception.ReservationException;
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
class ReservationController {

    private final ReservationFacade reservationFacade;

    private final Clock clock = Clock.systemUTC();

    @PostMapping("/screenings-tickets")
    TicketDTO reserveTicket(@RequestBody @Valid ReserveScreeningTicketDTO dto) {
        return reservationFacade.reserveTicket(dto, clock);
    }

    @PatchMapping("/screenings-tickets/{ticketUUID}/cancel")
    void cancelTicket(@PathVariable UUID ticketUUID) {
        reservationFacade.cancelTicket(ticketUUID, clock);
    }

    @GetMapping("/screenings-tickets")
    List<TicketDTO> readAllTickets() {
        return reservationFacade.readAllTickets();
    }
}

@RestControllerAdvice
class ReservationExceptionHandler {

    @ExceptionHandler(ReservationException.class)
    ResponseEntity<?> handle(ReservationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
