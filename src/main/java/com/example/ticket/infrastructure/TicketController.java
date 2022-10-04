package com.example.ticket.infrastructure;

import com.example.ticket.domain.TicketFacade;
import com.example.ticket.domain.dto.ReserveTicketDTO;
import com.example.ticket.domain.dto.TicketDTO;
import com.example.ticket.domain.exception.TicketException;
import com.example.ticket.domain.exception.TicketNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@AllArgsConstructor
class TicketController {

    private final TicketFacade ticketFacade;

    @PostMapping("/tickets/reservation")
    TicketDTO reserve(@RequestBody @Valid ReserveTicketDTO dto) {
        return ticketFacade.reserve(dto);
    }

    @PatchMapping("/tickets/{ticketId}/reservation/cancelled")
    void cancel(@PathVariable Long ticketId) {
        ticketFacade.cancel(ticketId, Clock.system(ZoneOffset.UTC));
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
