package code.catalog.infrastructure.rest;

import code.catalog.application.commands.FilmCreateCommand;
import code.catalog.application.commands.ScreeningCreateCommand;
import code.catalog.application.dto.FilmDto;
import code.catalog.application.dto.RoomDto;
import code.catalog.application.dto.ScreeningDto;
import code.catalog.application.dto.SeatDto;
import code.catalog.application.services.FilmCreateService;
import code.catalog.application.services.FilmReadService;
import code.catalog.application.services.RoomReadService;
import code.catalog.application.services.ScreeningCreateService;
import code.catalog.application.services.SeatReadService;
import code.catalog.domain.FilmCategory;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmRestController {

    private final FilmCreateService filmCreateHandler;

    private final FilmReadService filmReadService;

    private final ScreeningCreateService screeningCreateHandler;

    private final SeatReadService screeningSeatReadHandler;

    private final RoomReadService roomReadHandler;

    @PostMapping
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid FilmCreateCommand dto) {
        var createdFilm = filmCreateHandler.creteFilm(dto);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @GetMapping("/screenings")
    public List<FilmDto> readAll() {
        return filmReadService.readAll();
    }

    @GetMapping("/screenings/by/category")
    public List<FilmDto> readByCategory(@RequestParam FilmCategory category) {
        return filmReadService.readByCategory(category);
    }

    @GetMapping("/screenings/by/title")
    public FilmDto readByTitle(@RequestParam String title) {
        return filmReadService.readByTitle(title);
    }

    @PostMapping("/{filmId}/screenings")
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
        var createdScreening = screeningCreateHandler.createScreening(cmd);
        return new ResponseEntity<>(createdScreening, HttpStatus.CREATED);
    }

    @GetMapping("/screenings/{screeningId}/seats")
    public List<SeatDto> readSeats(@PathVariable Long screeningId) {
        return screeningSeatReadHandler.readByScreeningId(screeningId);
    }

    @GetMapping("/screenings/rooms")
    public List<RoomDto> readAllRooms() {
        return roomReadHandler.readAll();
    }
}

