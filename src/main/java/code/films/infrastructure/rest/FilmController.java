package code.films.infrastructure.rest;

import code.films.client.dto.SeatDto;
import code.films.client.commands.CreateFilmCommand;
import code.films.client.commands.CreateScreeningCommand;
import code.films.client.commands.handlers.CreateFilmHandler;
import code.films.client.commands.handlers.CreateScreeningHandler;
import code.films.client.dto.FilmDto;
import code.films.client.dto.ScreeningDto;
import code.films.client.queries.GetFilmsQuery;
import code.films.client.queries.GetScreeningSeatsQuery;
import code.films.client.queries.GetScreeningsQuery;
import code.films.client.queries.handlers.GetFilmsHandler;
import code.films.client.queries.handlers.GetScreeningHandler;
import code.films.client.queries.handlers.GetScreeningSeatsHandler;
import code.films.domain.FilmCategory;
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
public class FilmController {

    private final CreateFilmHandler createFilmHandler;

    private final GetFilmsHandler getFilmsHandler;

    private final CreateScreeningHandler createScreeningHandler;

    private final GetScreeningHandler getScreeningHandler;

    private final GetScreeningSeatsHandler getScreeningSeatsHandler;

    @PostMapping("/films")
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid CreateFilmCommand dto) {
        var createdFilm = createFilmHandler.handle(dto);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @GetMapping("/films")
    public List<FilmDto> searchFilmsBy(@RequestParam(required = false) FilmCategory category) {
        var params = GetFilmsQuery
                .builder()
                .category(category)
                .build();
        return getFilmsHandler.handle(params);
    }

    @PostMapping("/films/screenings")
    public ResponseEntity<ScreeningDto> createScreening(
            @RequestBody
            @Valid
            CreateScreeningCommand dto
    ) {
        var createdScreening = createScreeningHandler.createScreening(dto);
        return new ResponseEntity<>(createdScreening, HttpStatus.CREATED);
    }

    @GetMapping("/films/screenings")
    public List<ScreeningDto> searchScreeningsBy(
            @RequestParam(required = false)
            UUID filmId,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime date
    ) {

        var params = GetScreeningsQuery
                .builder()
                .filmId(filmId)
                .date(date)
                .build();

        return getScreeningHandler.handle(params);
    }

    @GetMapping("/films/screenings/{screeningId}/seats")
    public List<SeatDto> searchSeats(@PathVariable UUID screeningId) {
        return getScreeningSeatsHandler.handle(
                GetScreeningSeatsQuery
                        .builder()
                        .screeningId(screeningId)
                        .build()
        );
    }
}

