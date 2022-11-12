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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
class ScreeningController {

    private final ScreeningFacade ticketFacade;

    @PostMapping("/screenings")
    ScreeningDTO add(
            @RequestBody
            @Valid
            AddScreeningDTO dto
    ) {
        return ticketFacade.add(dto);
    }

    @GetMapping("/screenings")
    List<ScreeningDTO> searchBy(
            @RequestParam(required = false)
            UUID filmId,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime date
    ) {
        var params = new HashMap<String, Object>() {{
            put("filmId", filmId);
            put("date", date);
        }};
        return ticketFacade.searchBy(params);
    }

    @PostMapping("/screenings-rooms")
    ScreeningRoomDTO addRoom(
            @RequestBody
            @Valid
            AddScreeningRoomDTO dto
    ) {
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

