package code.screening.infrastructure;

import code.screening.ScreeningFacade;
import code.screening.dto.*;
import code.screening.exception.ScreeningException;
import code.screening.exception.ScreeningNotFoundException;
import code.screening.exception.TicketException;
import code.screening.exception.TicketNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
class ScreeningController {

    private final ScreeningFacade ticketFacade;

    private final Year currentYear;

    private final Clock clock;

    @PostMapping("/screenings")
    ScreeningDTO add(@RequestBody @Valid AddScreeningDTO dto) {
        return ticketFacade.add(dto, currentYear);
    }

    @GetMapping("/screenings")
    List<ScreeningDTO> readAll() {
        return ticketFacade.readAll();
    }

    @GetMapping("/screenings/{filmId}")
    List<ScreeningDTO> readByFilmId(@PathVariable Long filmId) {
        return ticketFacade.readByFilmId(filmId);
    }

    @GetMapping("/screenings/date")
    List<ScreeningDTO> readByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime date) {
        return ticketFacade.readByDate(date, currentYear);
    }

    @PostMapping("/screening-rooms")
    ScreeningRoomDTO addRoom(@RequestBody @Valid AddScreeningRoomDTO dto) {
        return ticketFacade.addRoom(dto);
    }

    @GetMapping("/screening-rooms")
    List<ScreeningRoomDTO> readAllRooms() {
        return ticketFacade.readAllRooms();
    }

    @PostMapping("/tickets")
    TicketDTO bookTicket(@RequestBody @Valid BookTicketDTO dto) {
        return ticketFacade.bookTicket(dto, clock);
    }

    @PatchMapping("/tickets/{ticketUUID}/cancelled")
    void cancelTicket(@PathVariable UUID ticketUUID) {
        ticketFacade.cancelTicket(ticketUUID, clock);
    }

    @GetMapping("/tickets")
    List<TicketDTO> readAllTickets() {
        return ticketFacade.readAllTickets();
    }
}

@RestControllerAdvice
class ScreeningExceptionHandler {

    @ExceptionHandler(ScreeningException.class)
    ResponseEntity<String> handle(ScreeningException exception) {
        if (exception instanceof ScreeningNotFoundException) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
