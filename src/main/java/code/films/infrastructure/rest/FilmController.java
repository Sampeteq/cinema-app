package code.films.infrastructure.rest;

import code.films.application.dto.*;
import code.films.application.FilmFacade;
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

    private final FilmFacade filmFacade;

    @PostMapping("/films")
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid CreateFilmDto dto) {
        var createdFilm = filmFacade.createFilm(dto);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @GetMapping("/films")
    public List<FilmDto> searchFilmsBy(@RequestParam(required = false) FilmCategory category) {
        var params = FilmSearchParams
                .builder()
                .category(category)
                .build();
        return filmFacade.searchFilmsBy(params);
    }

    @PostMapping("/films/screenings/rooms")
    public RoomDto createRoom(@RequestBody @Valid CreateRoomDto dto) {
        return filmFacade.createRoom(dto);
    }

    @GetMapping("/films/screenings/rooms")
    public List<RoomDto> searchAllRooms() {
        return filmFacade.searchAllRooms();
    }

    @PostMapping("/films/screenings")
    public ResponseEntity<ScreeningDto> createScreening(
            @RequestBody
            @Valid
            CreateScreeningDto dto
    ) {
        var createdScreening = filmFacade.createScreening(dto);
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

        var params = ScreeningSearchParams
                .builder()
                .filmId(filmId)
                .date(date)
                .build();

        return filmFacade.searchScreeningsBy(params);
    }

    @GetMapping("/films/screenings/{screeningId}/seats")
    public List<SeatDto> searchSeats(@PathVariable UUID screeningId) {
        return filmFacade.searchSeats(screeningId);
    }
}

