package code.films.infrastructure.rest;

import code.films.application.dto.FilmSearchParams;
import code.films.application.FilmFacade;
import code.films.domain.FilmCategory;
import code.films.application.dto.CreateFilmDto;
import code.films.application.dto.FilmDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class FilmCrudController {

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
}

