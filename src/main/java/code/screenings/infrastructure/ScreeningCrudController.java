package code.screenings.infrastructure;

import code.screenings.ScreeningFacade;
import code.screenings.exception.ScreeningException;
import code.screenings.exception.ScreeningNotFoundException;
import code.screenings.exception.ScreeningRoomNotFoundException;
import code.screenings.dto.ScreeningCreatingRequest;
import code.screenings.dto.ScreeningRoomCreatingRequest;
import code.screenings.dto.ScreeningRoomView;
import code.screenings.dto.ScreeningSearchParamsView;
import code.screenings.dto.ScreeningView;
import code.screenings.dto.SeatView;
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
    ScreeningView add(
            @RequestBody
            @Valid
            ScreeningCreatingRequest request
    ) {
        return screeningFacade.createScreening(request);
    }

    @GetMapping("/screenings")
    List<ScreeningView> searchBy(
            @RequestParam(required = false)
            UUID filmId,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime date
    ) {

        var paramsDto = ScreeningSearchParamsView
                .builder()
                .filmId(filmId)
                .screeningDate(date)
                .build();

        return screeningFacade.searchScreeningsBy(paramsDto);
    }

    @GetMapping("/screenings/{screeningId}/seats")
    List<SeatView> searchSeats(@PathVariable UUID screeningId) {
        return screeningFacade.searchScreeningSeats(screeningId);
    }

    @PostMapping("/screenings-rooms")
    ScreeningRoomView createRoom(
            @RequestBody
            @Valid
            ScreeningRoomCreatingRequest request
    ) {
        return screeningFacade.createRoom(request);
    }

    @GetMapping("/screenings-rooms")
    List<ScreeningRoomView> searchAllRooms() {
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

