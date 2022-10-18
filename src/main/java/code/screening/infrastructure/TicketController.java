package code.screening.infrastructure;

import code.screening.TicketFacade;
import code.screening.dto.BookTicketDTO;
import code.screening.dto.TicketDTO;
import code.screening.exception.TicketException;
import code.screening.exception.TicketNotFoundException;
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
class TicketController {

    private final TicketFacade ticketFacade;

    private final Clock clock;

    @PostMapping("/tickets")
    TicketDTO book(@RequestBody @Valid BookTicketDTO dto) {
        return ticketFacade.book(dto, clock);
    }

    @PatchMapping("/tickets/{ticketUUID}/cancelled")
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
