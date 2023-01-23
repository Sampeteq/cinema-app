package code.screenings.infrastructure;

import code.screenings.ScreeningFacade;
import code.screenings.dto.CreateFilmDto;
import code.screenings.dto.FilmCategoryDto;
import code.screenings.dto.FilmDto;
import code.screenings.dto.FilmSearchParamsDto;
import code.screenings.exception.FilmException;
import code.screenings.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
class FilmCrudController {

    private final ScreeningFacade screeningFacade;

    @PostMapping("/films")
    FilmDto createFilm(
            @RequestBody
            @Valid
            CreateFilmDto dto
    ) {
        return screeningFacade.createFilm(dto);
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
        return screeningFacade.searchFilmsBy(params);
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
