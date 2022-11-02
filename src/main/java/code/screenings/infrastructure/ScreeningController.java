package code.screenings.infrastructure;

import code.screenings.ScreeningFacade;
import code.screenings.dto.AddScreeningDTO;
import code.screenings.dto.AddScreeningRoomDTO;
import code.screenings.dto.ScreeningDTO;
import code.screenings.dto.ScreeningRoomDTO;
import code.screenings.exception.ScreeningException;
import code.screenings.exception.ScreeningNotFoundException;
import code.screenings.exception.ScreeningRoomNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashMap;
import java.util.List;

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
    List<ScreeningDTO> searchBy(@RequestParam(required = false) Long filmId,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime date) {
        var searchParams = new HashMap<String, Object>() {{
           put("filmId", filmId);
           put("date", date);
        }};
        return ticketFacade.searchBy(searchParams);
    }

    @PostMapping("/screenings-rooms")
    ScreeningRoomDTO addRoom(@RequestBody @Valid AddScreeningRoomDTO dto) {
        return ticketFacade.addRoom(dto);
    }

    @GetMapping("/screenings-rooms")
    List<ScreeningRoomDTO> readAllRooms() {
        return ticketFacade.readAllRooms();
    }
}

@RestControllerAdvice
class ScreeningExceptionHandler {

    @ExceptionHandler(ScreeningException.class)
    ResponseEntity<String> handle(ScreeningException exception) {
        if (exception instanceof ScreeningNotFoundException || exception instanceof ScreeningRoomNotFoundException) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

