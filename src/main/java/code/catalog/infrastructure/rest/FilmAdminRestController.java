package code.catalog.infrastructure.rest;

import code.catalog.application.commands.FilmCreateCommand;
import code.catalog.application.commands.ScreeningCreateCommand;
import code.catalog.application.dto.FilmDto;
import code.catalog.application.dto.ScreeningDto;
import code.catalog.application.services.FilmCreateService;
import code.catalog.application.services.ScreeningCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmAdminRestController {

    private final FilmCreateService filmCreateHandler;

    private final ScreeningCreateService screeningCreateHandler;

    @PostMapping
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid FilmCreateCommand dto) {
        var createdFilm = filmCreateHandler.creteFilm(dto);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
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
}
