package code.films.infrastructure.rest;

import code.bookings.application.dto.SeatDto;
import code.films.applications.dto.CreateFilmDto;
import code.films.applications.dto.CreateScreeningDto;
import code.films.applications.dto.FilmDto;
import code.films.applications.dto.FilmSearchParams;
import code.films.applications.dto.ScreeningDto;
import code.films.applications.dto.ScreeningSearchParams;
import code.films.applications.services.FilmCreateService;
import code.films.applications.services.FilmSearchService;
import code.films.applications.services.ScreeningCreateService;
import code.films.applications.services.ScreeningSearchService;
import code.films.applications.services.mappers.FilmMapper;
import code.films.applications.services.mappers.ScreeningMapper;
import code.films.domain.FilmCategory;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class FilmController {

    private final FilmCreateService filmCreateService;

    private final FilmMapper filmMapper;

    private final FilmSearchService filmSearchService;

    private final ScreeningCreateService screeningCreateService;

    private final ScreeningMapper screeningMapper;

    private final ScreeningSearchService screeningSearchService;

    @PostMapping("/films")
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid CreateFilmDto dto) {
        var createdFilm = filmCreateService.createFilm(dto);
        return new ResponseEntity<>(filmMapper.mapToDto(createdFilm), HttpStatus.CREATED);
    }

    @GetMapping("/films")
    public List<FilmDto> searchFilmsBy(@RequestParam(required = false) FilmCategory category) {
        var params = FilmSearchParams
                .builder()
                .category(category)
                .build();
        return filmSearchService.searchFilmsBy(params);
    }

    @PostMapping("/films/screenings")
    public ResponseEntity<ScreeningDto> createScreening(
            @RequestBody
            @Valid
            CreateScreeningDto dto
    ) {
        var createdScreening = screeningCreateService.createScreening(dto);
        return new ResponseEntity<>(screeningMapper.mapToDto(createdScreening), HttpStatus.CREATED);
    }

    @GetMapping("/films/screenings")
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

        return screeningSearchService.searchScreeningsBy(params);
    }

    @GetMapping("/films/screenings/{screeningId}/seats")
    public List<SeatDto> searchSeats(@PathVariable UUID screeningId) {
        return screeningSearchService.searchSeats(screeningId);
    }
}

