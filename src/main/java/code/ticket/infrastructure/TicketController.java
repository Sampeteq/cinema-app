package code.ticket.infrastructure;

import code.ticket.TicketFacade;
import code.ticket.dto.BookTicketDTO;
import code.ticket.dto.TicketDTO;
import code.ticket.exception.TicketException;
import code.ticket.exception.TicketNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
class TicketController {

    private final TicketFacade ticketFacade;

    private final Clock clock;

    @PostMapping("/tickets/booking")
    TicketDTO reserve(@RequestBody @Valid BookTicketDTO dto) {
        return ticketFacade.book(dto, clock);
    }

    @PatchMapping("/tickets/{ticketUUID}/booking/cancelled")
    void cancel(@PathVariable UUID ticketUUID) {
        ticketFacade.cancel(ticketUUID, clock);
    }

    @GetMapping("/tickets")
    List<TicketDTO> readAll() {
        return ticketFacade.readAll();
    }
}

@RestControllerAdvice
class TicketExceptionHandler {

    @ExceptionHandler(TicketException.class)
    ResponseEntity<?> handle(TicketException exception) {
        if (exception instanceof TicketNotFoundException) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
