package code.screenings.infrastructure;

import code.screenings.ScreeningFacade;
import code.screenings.dto.*;
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
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
class ScreeningCrudController {

    private final ScreeningFacade screeningFacade;

    @PostMapping("/screenings")
    ScreeningDto add(
            @RequestBody
            @Valid
            CreateScreeningDto dto
    ) {
        return screeningFacade.createScreening(dto);
    }

    @GetMapping("/screenings")
    List<ScreeningDto> searchBy(
            @RequestParam(required = false)
            UUID filmId,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime date
    ) {

        var paramsDto = ScreeningSearchParamsDto
                .builder()
                .filmId(filmId)
                .screeningDate(date)
                .build();

        return screeningFacade.searchScreeningsBy(paramsDto);
    }

    @PostMapping("/screenings-rooms")
    ScreeningRoomDto createRoom(
            @RequestBody
            @Valid
            CreateScreeningRoomDto dto
    ) {
        return screeningFacade.createRoom(dto);
    }

    @GetMapping("/screenings-rooms")
    List<ScreeningRoomDto> searchAllRooms() {
        return screeningFacade.searchAllRooms();
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

