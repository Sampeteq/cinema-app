package code.films.infrastructure.rest;

import code.films.application.FilmFacade;
import code.films.application.ScreeningSearchParams;
import code.films.application.dto.CreateScreeningDto;
import code.films.application.dto.ScreeningDto;
import code.films.application.dto.SeatDto;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class ScreeningCrudController {

    private final FilmFacade filmFacade;

    @PostMapping("/screenings")
    public ResponseEntity<ScreeningDto> createScreening(
            @RequestBody
            @Valid
            CreateScreeningDto dto
    ) {
        var createdScreening = filmFacade.createScreening(dto);
        return new ResponseEntity<>(createdScreening, HttpStatus.CREATED);
    }

    @GetMapping("/screenings")
    public List<ScreeningDto> searchScreeningsBy(
            @RequestParam(required = false)
            UUID filmId,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime date
    ) {

        var params = ScreeningSearchParams
                .builder()
                .filmId(filmId)
                .date(date)
                .build();

        return filmFacade.searchScreeningsBy(params);
    }

    @GetMapping("/screenings/{screeningId}/seats")
    public List<SeatDto> searchSeats(@PathVariable UUID screeningId) {
        return filmFacade.searchSeats(screeningId);
    }
}

