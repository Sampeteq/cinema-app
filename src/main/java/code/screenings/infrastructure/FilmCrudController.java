package code.screenings.infrastructure;

import code.screenings.ScreeningFacade;
import code.screenings.dto.FilmCreatingRequest;
import code.screenings.dto.FilmCategoryView;
import code.screenings.dto.FilmView;
import code.screenings.dto.FilmSearchParamsView;
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
    FilmView createFilm(
            @RequestBody
            @Valid
            FilmCreatingRequest request
    ) {
        return screeningFacade.createFilm(request);
    }

    @GetMapping("/films")
    List<FilmView> searchFilms(
            @RequestParam(required = false)
            FilmCategoryView category
    ) {
        var params = FilmSearchParamsView
                .builder()
                .category(category)
                .build();
        return screeningFacade.searchFilms(params);
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
