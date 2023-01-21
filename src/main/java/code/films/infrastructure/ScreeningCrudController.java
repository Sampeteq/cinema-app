package code.films.infrastructure;

import code.films.FilmFacade;
import code.films.dto.*;
import code.films.exception.ScreeningException;
import code.films.exception.ScreeningNotFoundException;
import code.films.exception.ScreeningRoomNotFoundException;
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

    private final FilmFacade filmFacade;

    @PostMapping("/screenings")
    ScreeningDto createScreening(
            @RequestBody
            @Valid
            CreateScreeningDto request
    ) {
        return filmFacade.createScreening(request);
    }

    @GetMapping("/screenings")
    List<ScreeningDto> searchScreeningsBy(
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

        return filmFacade.searchScreeningsBy(paramsDto);
    }

    @GetMapping("/screenings/{screeningId}/seats")
    List<SeatDto> searchScreeningSeats(@PathVariable UUID screeningId) {
        return filmFacade.searchScreeningSeats(screeningId);
    }

    @PostMapping("/screenings-rooms")
    ScreeningRoomDto createScreeningsRoom(
            @RequestBody
            @Valid
            CreateScreeningRoomDto dto
    ) {
        return filmFacade.createScreeningsRoom(dto);
    }

    @GetMapping("/screenings-rooms")
    List<ScreeningRoomDto> searchScreeningsRooms() {
        return filmFacade.searchScreeningsRooms();
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

