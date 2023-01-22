package code.bookings.infrastructure;

import code.bookings.BookingFacade;
import code.bookings.dto.*;
import code.bookings.exception.ScreeningException;
import code.bookings.exception.ScreeningNotFoundException;
import code.bookings.exception.ScreeningRoomNotFoundException;
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

    private final BookingFacade bookingFacade;

    @PostMapping("/screenings")
    ScreeningDto createScreening(
            @RequestBody
            @Valid
            CreateScreeningDto request
    ) {
        return bookingFacade.createScreening(request);
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

        return bookingFacade.searchScreeningsBy(paramsDto);
    }

    @GetMapping("/screenings/{screeningId}/seats")
    List<SeatDto> searchScreeningSeats(@PathVariable UUID screeningId) {
        return bookingFacade.searchScreeningSeats(screeningId);
    }

    @PostMapping("/rooms")
    RoomDto createScreeningsRoom(
            @RequestBody
            @Valid
            CreateRoomDto dto
    ) {
        return bookingFacade.createScreeningsRoom(dto);
    }

    @GetMapping("/rooms")
    List<RoomDto> searchScreeningsRooms() {
        return bookingFacade.searchScreeningsRooms();
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

