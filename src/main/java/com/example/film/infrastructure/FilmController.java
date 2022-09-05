package com.example.film.infrastructure;

import com.example.film.domain.FilmAPI;
import com.example.film.domain.FilmCategory;
import com.example.film.domain.dto.AddFilmDTO;
import com.example.film.domain.dto.FilmDTO;
import com.example.film.domain.exception.FilmNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
class FilmController {

    private final FilmAPI filmAPI;

    @PostMapping("/films")
    FilmDTO addFilm(@RequestBody @Valid AddFilmDTO dto) {
        return filmAPI.addFilm(dto);
    }

    @GetMapping("/films")
    List<FilmDTO> readAllFilms() {
        return filmAPI.readAllFilms();
    }

    @GetMapping("/films/category")
    List<FilmDTO> readAllFilmsByCategory(@RequestParam FilmCategory category) {
        return filmAPI.readFilmsByCategory(category);
    }
}

@RestControllerAdvice
class FilmErrorHandler {

    @ExceptionHandler(FilmNotFoundException.class)
    ResponseEntity<?> handle(FilmNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
