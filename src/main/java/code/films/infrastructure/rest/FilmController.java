package code.films.infrastructure.rest;

import code.bookings.infrastructure.rest.dto.SeatDto;
import code.bookings.infrastructure.rest.dto.mappers.SeatMapper;
import code.films.domain.commands.CreateFilmCommand;
import code.films.domain.commands.CreateScreeningCommand;
import code.films.domain.queries.SearchFilmsQuery;
import code.films.domain.queries.SearchScreeningSeatsQuery;
import code.films.domain.queries.SearchScreeningsQuery;
import code.films.domain.commands.handlers.CreateFilmCommandHandler;
import code.films.domain.queries.handlers.SearchFilmsQueryHandler;
import code.films.domain.commands.handlers.CreateScreeningCommandHandler;
import code.films.domain.queries.handlers.SearchScreeningQueryHandler;
import code.films.domain.queries.handlers.SearchScreeningSeatsQueryHandler;
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

    private final SearchFilmsQueryHandler searchFilmsQueryHandler;

    private final CreateScreeningCommandHandler createScreeningCommandHandler;

    private final ScreeningMapper screeningMapper;

    private final SearchScreeningQueryHandler searchScreeningQueryHandler;

    private final SearchScreeningSeatsQueryHandler searchScreeningSeatsQueryHandler;

    private final SeatMapper seatMapper;

    @PostMapping("/films")
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid CreateFilmCommand dto) {
        var createdFilm = createFilmCommandHandler.handle(dto);
        return new ResponseEntity<>(filmMapper.mapToDto(createdFilm), HttpStatus.CREATED);
    }

    @GetMapping("/films")
    public List<FilmDto> searchFilmsBy(@RequestParam(required = false) FilmCategory category) {
        var params = SearchFilmsQuery
                .builder()
                .category(category)
                .build();
        return searchFilmsQueryHandler.handle(params);
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

        var params = SearchScreeningsQuery
                .builder()
                .filmId(filmId)
                .date(date)
                .build();

        return searchScreeningQueryHandler.handle(params);
    }

    @GetMapping("/films/screenings/{screeningId}/seats")
    public List<SeatDto> searchSeats(@PathVariable UUID screeningId) {
        return searchScreeningSeatsQueryHandler.handle(
                SearchScreeningSeatsQuery
                        .builder()
                        .screeningId(screeningId)
                        .build()
        )
                .stream()
                .map(seatMapper::toDto)
                .toList();
    }
}

