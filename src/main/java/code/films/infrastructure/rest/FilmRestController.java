package code.films.infrastructure.rest;

import code.films.application.commands.FilmCreateCommand;
import code.films.application.dto.FilmDto;
import code.films.application.handlers.FilmCreateHandler;
import code.films.application.handlers.FilmReadAllHandler;
import code.films.application.handlers.FilmReadByCategoryHandler;
import code.films.application.handlers.FilmReadByTitleHandler;
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
public class FilmRestController {

    private final FilmCreateHandler filmCreateHandler;

    private final FilmReadAllHandler filmReadAllHandler;

    private final FilmReadByTitleHandler filmReadByTitleHandler;

    private final FilmReadByCategoryHandler filmReadByCategoryHandler;

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
}

