package com.example.film.infrastructure;

import com.example.film.FilmCategory;
import com.example.film.FilmFacade;
import com.example.film.dto.AddFilmDTO;
import com.example.film.dto.FilmDTO;
import com.example.film.exception.FilmException;
import com.example.film.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
class FilmController {

    private final FilmFacade filmFacade;

    @PostMapping("/films")
    FilmDTO add(@RequestBody @Valid AddFilmDTO dto) {
        return filmFacade.add(dto);
    }

    @GetMapping("/films")
    List<FilmDTO> readAll() {
        return filmFacade.readAll();
    }

    @GetMapping("/films/category")
    List<FilmDTO> readAllByCategory(@RequestParam FilmCategory category) {
        return filmFacade.readAllByCategory(category);
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
