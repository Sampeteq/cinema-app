package com.example.screening.infrastructure;

import com.example.film.domain.FilmId;
import com.example.screening.domain.ScreeningAPI;
import com.example.screening.domain.ScreeningDate;
import com.example.screening.domain.dto.AddScreeningDTO;
import com.example.screening.domain.dto.ScreeningDTO;
import com.example.screening.domain.exception.ScreeningException;
import com.example.screening.domain.exception.ScreeningNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
class ScreeningController {

    private final ScreeningAPI screeningAPI;

    private final Year currentYear;

    @PostMapping("/screenings")
    ScreeningDTO addNewScreening(@RequestBody @Valid AddScreeningDTO dto) {
        return screeningAPI.addScreening(dto, currentYear);
    }

    @GetMapping("/screenings")
    List<ScreeningDTO> readAllScreenings() {
        return screeningAPI.readAllScreenings();
    }

    @GetMapping("/screenings/{filmId}")
    List<ScreeningDTO> readScreeningsByFilmId(@PathVariable UUID filmId) {
        return screeningAPI.readScreeningsByFilmId(
                FilmId.of(filmId)
        );
    }

    @GetMapping("/screenings/date")
    List<ScreeningDTO> readScreeningsByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime date) {
        return screeningAPI.readAllScreeningsByDate(ScreeningDate.of(date, currentYear));
    }
}

@RestControllerAdvice
class ScreeningExceptionHandler {

    @ExceptionHandler(ScreeningException.class)
    ResponseEntity<String> handle(ScreeningException exception) {
        if (exception instanceof ScreeningNotFoundException) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
