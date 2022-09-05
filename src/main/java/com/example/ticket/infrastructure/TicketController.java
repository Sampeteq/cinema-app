package com.example.ticket.infrastructure;

import com.example.screening.domain.exception.NoScreeningFreeSeatsException;
import com.example.ticket.domain.TicketAPI;
import com.example.ticket.domain.dto.ReserveTicketDTO;
import com.example.ticket.domain.dto.TicketDTO;
import com.example.ticket.domain.exception.TooLateToCancelTicketReservationException;
import com.example.ticket.domain.exception.WrongTicketAgeException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
class TicketController {

    private final TicketAPI ticketAPI;

    @PostMapping("/tickets/reservation")
    TicketDTO reserveTicket(@RequestBody @Valid ReserveTicketDTO cmd) {
        return ticketAPI.reserveTicket(cmd);
    }

    @PatchMapping("/tickets/{ticketId}/reservation/cancelled")
    void cancelTicket(@PathVariable UUID ticketId) {
        ticketAPI.cancel(ticketId, LocalDateTime.now());
    }

    @GetMapping("/tickets")
    List<TicketDTO> readAllTickets() {
        return ticketAPI.readAllTickets();
    }
}

@RestControllerAdvice
class TicketErrorHandler {

    @ExceptionHandler(NoScreeningFreeSeatsException.class)
    ResponseEntity<?> handle(NoScreeningFreeSeatsException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongTicketAgeException.class)
    ResponseEntity<?> handle(WrongTicketAgeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TooLateToCancelTicketReservationException.class)
    ResponseEntity<?> handle(TooLateToCancelTicketReservationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
