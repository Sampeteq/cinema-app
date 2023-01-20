package code.films.infrastructure;

import code.films.FilmFacade;
import code.films.dto.FilmCategoryDto;
import code.films.dto.CreateFilmDto;
import code.films.dto.FilmSearchParamsDto;
import code.films.dto.FilmDto;
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
    FilmDto createFilm(
            @RequestBody
            @Valid
            CreateFilmDto dto
    ) {
        return filmFacade.createFilm(dto);
    }

    @GetMapping("/films")
    List<FilmDto> searchFilms(
            @RequestParam(required = false)
            FilmCategoryDto category
    ) {
        var params = FilmSearchParamsDto
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
