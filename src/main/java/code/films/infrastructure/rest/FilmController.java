package code.films.infrastructure.rest;

import code.films.application.commands.FilmCreationCommand;
import code.films.application.handlers.FilmCreationHandler;
import code.films.application.dto.FilmDto;
import code.films.application.handlers.FilmReadingHandler;
import code.films.application.queries.FilmReadingQuery;
import code.films.domain.FilmCategory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class FilmController {

    private final FilmCreationHandler filmCreationHandler;

    private final FilmReadingHandler filmReadingHandler;

    @PostMapping("/films")
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid FilmCreationCommand dto) {
        var createdFilm = filmCreationHandler.handle(dto);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @GetMapping("/films")
    public List<FilmDto> searchFilmsBy(@RequestParam(required = false) FilmCategory category) {
        var params = FilmReadingQuery
                .builder()
                .category(category)
                .build();
        return filmReadingHandler.handle(params);
    }
}

