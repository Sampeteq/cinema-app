package code.films.infrastructure.rest;

import code.films.application.commands.FilmScreeningCreateCommand;
import code.films.application.dto.FilmScreeningDto;
import code.films.application.dto.FilmScreeningSeatDto;
import code.films.application.handlers.FilmScreeningCreateHandler;
import code.films.application.handlers.FilmScreeningReadHandler;
import code.films.application.handlers.FilmScreeningSeatReadHandler;
import code.films.application.queries.FilmScreeningReadQuery;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/screenings")
@RequiredArgsConstructor
public class FilmScreeningRestController {

    private final FilmScreeningCreateHandler screeningCreateHandler;

    private final FilmScreeningReadHandler screeningReadHandler;

    private final FilmScreeningSeatReadHandler screeningSeatReadHandler;

    @PostMapping
    public ResponseEntity<FilmScreeningDto> createScreening(
            @RequestBody
            @Valid
            FilmScreeningCreateCommand cmd
    ) {
        var createdScreening = screeningCreateHandler.handle(cmd);
        return new ResponseEntity<>(createdScreening, HttpStatus.CREATED);
    }

    @GetMapping
    public List<FilmScreeningDto> readScreeningsBy(
            @RequestParam(required = false)
            String filmTitle,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime date
    ) {
        var query = FilmScreeningReadQuery
                .builder()
                .filmTitle(filmTitle)
                .date(date)
                .build();
        return screeningReadHandler.handle(query);
    }

    @GetMapping("/{screeningId}/seats")
    public List<FilmScreeningSeatDto> readSeats(@PathVariable Long screeningId) {
        return screeningSeatReadHandler.handle(screeningId);
    }
}
