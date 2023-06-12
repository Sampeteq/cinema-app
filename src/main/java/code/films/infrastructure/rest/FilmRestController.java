package code.films.infrastructure.rest;

import code.films.application.commands.FilmCreateCommand;
import code.films.application.commands.ScreeningCreateCommand;
import code.films.application.dto.FilmDto;
import code.films.application.dto.ScreeningDto;
import code.films.application.dto.RoomDto;
import code.films.application.dto.SeatDto;
import code.films.application.handlers.FilmCreateHandler;
import code.films.application.handlers.FilmReadAllHandler;
import code.films.application.handlers.FilmReadByCategoryHandler;
import code.films.application.handlers.FilmReadByTitleHandler;
import code.films.application.handlers.ScreeningCreateHandler;
import code.films.application.handlers.RoomReadHandler;
import code.films.application.handlers.SeatReadHandler;
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

@RestController
@AllArgsConstructor
public class FilmRestController {

    private final FilmCreateHandler filmCreateHandler;

    private final FilmReadAllHandler filmReadAllHandler;

    private final FilmReadByTitleHandler filmReadByTitleHandler;

    private final FilmReadByCategoryHandler filmReadByCategoryHandler;

    private final ScreeningCreateHandler screeningCreateHandler;

    private final SeatReadHandler screeningSeatReadHandler;

    private final RoomReadHandler roomReadHandler;

    @PostMapping("/films")
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid FilmCreateCommand dto) {
        var createdFilm = filmCreateHandler.handle(dto);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @GetMapping("/films")
    public List<FilmDto> readAll() {
        return filmReadAllHandler.handle();
    }

    @GetMapping("/films/title")
    public FilmDto readByTitle(@RequestParam String title) {
        return filmReadByTitleHandler.handle(title);
    }

    @GetMapping("/films/category")
    public List<FilmDto> readByCategory(@RequestParam FilmCategory category) {
        return filmReadByCategoryHandler.handle(category);
    }

    @PostMapping("/films/{filmId}/screenings")
    public ResponseEntity<ScreeningDto> createScreening(
            @PathVariable
            Long filmId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime screeningDate
    ) {
        var cmd = ScreeningCreateCommand
                .builder()
                .filmId(filmId)
                .date(screeningDate)
                .build();
        var createdScreening = screeningCreateHandler.handle(cmd);
        return new ResponseEntity<>(createdScreening, HttpStatus.CREATED);
    }

    @GetMapping("/films/screenings/{screeningId}/seats")
    public List<SeatDto> readSeats(@PathVariable Long screeningId) {
        return screeningSeatReadHandler.handle(screeningId);
    }

    @GetMapping("/films/screenings/rooms")
    public List<RoomDto> readAllRooms() {
        return roomReadHandler.handle();
    }
}

