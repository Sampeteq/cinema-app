package com.example.screening.infrastructure;

import com.example.screening.domain.ScreeningDate;
import com.example.screening.domain.ScreeningFacade;
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

@RestController
@AllArgsConstructor
class ScreeningController {

    private final ScreeningFacade screeningFacade;

    private final Year currentYear;

    @PostMapping("/screenings")
    ScreeningDTO add(@RequestBody @Valid AddScreeningDTO dto) {
        return screeningFacade.add(dto, currentYear);
    }

    @GetMapping("/screenings")
    List<ScreeningDTO> readAll() {
        return screeningFacade.readAll();
    }

    @GetMapping("/screenings/{filmId}")
    List<ScreeningDTO> readByFilmId(@PathVariable Long filmId) {
        return screeningFacade.readAllByFilmId(filmId);
    }

    @GetMapping("/screenings/date")
    List<ScreeningDTO> readByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime date) {
        return screeningFacade.readAllByDate(ScreeningDate.of(date, currentYear));
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
