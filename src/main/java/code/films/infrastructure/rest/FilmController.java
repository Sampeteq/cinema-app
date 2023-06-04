package code.films.infrastructure.rest;

import code.films.application.commands.FilmCreateCommand;
import code.films.application.dto.FilmDto;
import code.films.application.handlers.FilmCreateHandler;
import code.films.application.handlers.FilmReadingHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class FilmController {

    private final FilmCreateHandler filmCreateHandler;

    private final FilmReadingHandler filmReadingHandler;

    @PostMapping("/films")
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid FilmCreateCommand dto) {
        var createdFilm = filmCreateHandler.handle(dto);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @GetMapping("/films")
    public List<FilmDto> searchFilmsBy() {
        return filmReadingHandler.handle();
    }
}

