package code.films.infrastructure;

import code.films.FilmFacade;
import code.films.dto.FilmCategoryView;
import code.films.dto.FilmCreatingRequest;
import code.films.dto.FilmSearchParamsView;
import code.films.dto.FilmView;
import code.films.exception.FilmException;
import code.films.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
class FilmCrudController {

    private final FilmFacade filmFacade;

    @PostMapping("/films")
    FilmView createFilm(
            @RequestBody
            @Valid
            FilmCreatingRequest request
    ) {
        return filmFacade.createFilm(request);
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
        return filmFacade.searchFilms(params);
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
