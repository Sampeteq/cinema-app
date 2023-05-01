package code.films.infrastructure.rest;

import code.films.client.commands.CreateFilmCommand;
import code.films.client.commands.handlers.CreateFilmHandler;
import code.films.client.dto.FilmDto;
import code.films.client.queries.GetFilmsQuery;
import code.films.client.queries.handlers.GetFilmsHandler;
import code.films.domain.FilmCategory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class FilmController {

    private final CreateFilmHandler createFilmHandler;

    private final GetFilmsHandler getFilmsHandler;

    @PostMapping("/films")
    public ResponseEntity<FilmDto> createFilm(@RequestBody @Valid CreateFilmCommand dto) {
        var createdFilm = createFilmHandler.handle(dto);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @GetMapping("/films")
    public List<FilmDto> searchFilmsBy(@RequestParam(required = false) FilmCategory category) {
        var params = GetFilmsQuery
                .builder()
                .category(category)
                .build();
        return getFilmsHandler.handle(params);
    }
}

