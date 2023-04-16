package code.films.infrastructure.rest;

import code.bookings.infrastructure.rest.dto.SeatDto;
import code.bookings.infrastructure.rest.dto.mappers.SeatMapper;
import code.films.domain.commands.CreateFilmCommand;
import code.films.domain.commands.CreateScreeningCommand;
import code.films.domain.queries.GetFilmsQuery;
import code.films.domain.queries.GetScreeningSeatsQuery;
import code.films.domain.queries.GetScreeningsQuery;
import code.films.domain.commands.handlers.CreateFilmCommandHandler;
import code.films.domain.queries.handlers.GetFilmsQueryHandler;
import code.films.domain.commands.handlers.CreateScreeningCommandHandler;
import code.films.domain.queries.handlers.GetScreeningQueryHandler;
import code.films.domain.queries.handlers.GetScreeningSeatsQueryHandler;
import code.films.infrastructure.rest.mappers.FilmMapper;
import code.films.infrastructure.rest.mappers.ScreeningMapper;
import code.films.domain.FilmCategory;
import code.films.infrastructure.rest.dto.FilmDto;
import code.films.infrastructure.rest.dto.ScreeningDto;
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

    private final CreateFilmCommandHandler createFilmCommandHandler;

    private final FilmMapper filmMapper;

    private final GetFilmsQueryHandler getFilmsQueryHandler;

    private final CreateScreeningCommandHandler createScreeningCommandHandler;

    private final ScreeningMapper screeningMapper;

    private final GetScreeningQueryHandler getScreeningQueryHandler;

    private final GetScreeningSeatsQueryHandler getScreeningSeatsQueryHandler;

    private final SeatMapper seatMapper;

    @PostMapping("/films")
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid CreateFilmCommand dto) {
        var createdFilm = createFilmCommandHandler.handle(dto);
        return new ResponseEntity<>(filmMapper.mapToDto(createdFilm), HttpStatus.CREATED);
    }

    @GetMapping("/films")
    public List<FilmDto> searchFilmsBy(@RequestParam(required = false) FilmCategory category) {
        var params = GetFilmsQuery
                .builder()
                .category(category)
                .build();
        return getFilmsQueryHandler.handle(params);
    }

    @PostMapping("/films/screenings")
    public ResponseEntity<ScreeningDto> createScreening(
            @RequestBody
            @Valid
            CreateScreeningCommand dto
    ) {
        var createdScreening = createScreeningCommandHandler.createScreening(dto);
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

        var params = GetScreeningsQuery
                .builder()
                .filmId(filmId)
                .date(date)
                .build();

        return getScreeningQueryHandler.handle(params);
    }

    @GetMapping("/films/screenings/{screeningId}/seats")
    public List<SeatDto> searchSeats(@PathVariable UUID screeningId) {
        return getScreeningSeatsQueryHandler.handle(
                GetScreeningSeatsQuery
                        .builder()
                        .screeningId(screeningId)
                        .build()
        )
                .stream()
                .map(seatMapper::toDto)
                .toList();
    }
}

