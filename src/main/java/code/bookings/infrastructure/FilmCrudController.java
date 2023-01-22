package code.bookings.infrastructure;

import code.bookings.BookingFacade;
import code.bookings.dto.CreateFilmDto;
import code.bookings.dto.FilmCategoryDto;
import code.bookings.dto.FilmDto;
import code.bookings.dto.FilmSearchParamsDto;
import code.bookings.exception.FilmException;
import code.bookings.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
class FilmCrudController {

    private final BookingFacade bookingFacade;

    @PostMapping("/films")
    FilmDto createFilm(
            @RequestBody
            @Valid
            CreateFilmDto dto
    ) {
        return bookingFacade.createFilm(dto);
    }

    @GetMapping("/films")
    List<FilmDto> searchFilmsBy(
            @RequestParam(required = false)
            FilmCategoryDto category
    ) {
        var params = FilmSearchParamsDto
                .builder()
                .category(category)
                .build();
        return bookingFacade.searchFilmsBy(params);
    }
}

@RestControllerAdvice
class FilmExceptionHandler {

    @ExceptionHandler(FilmException.class)
    ResponseEntity<String> handle(FilmException exception) {
        if (exception instanceof FilmNotFoundException) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
