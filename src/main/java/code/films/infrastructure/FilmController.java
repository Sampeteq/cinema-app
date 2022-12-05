package code.films.infrastructure;

import code.films.FilmFacade;
import code.films.dto.AddFilmDto;
import code.films.dto.FilmCategoryDto;
import code.films.dto.FilmDto;
import code.films.dto.FilmSearchParamDto;
import code.films.exception.FilmException;
import code.films.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@AllArgsConstructor
class FilmController {

    private final FilmFacade filmFacade;

    @PostMapping("/films")
    FilmDto add(
            @RequestBody
            @Valid
            AddFilmDto dto
    ) {
        return filmFacade.add(dto);
    }

    @GetMapping("/films")
    List<FilmDto> readAll(
            @RequestParam(required = false)
            FilmCategoryDto category
    ) {
        var readParameters = new HashMap<FilmSearchParamDto, Object>() {{
            put(FilmSearchParamDto.CATEGORY, category);
        }};
        return filmFacade.search(readParameters);
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
