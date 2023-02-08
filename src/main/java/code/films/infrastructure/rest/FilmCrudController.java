package code.films.infrastructure.rest;

import code.films.application.FilmSearchParams;
import code.films.application.FilmFacade;
import code.films.domain.FilmCategory;
import code.films.application.dto.CreateFilmDto;
import code.films.application.dto.FilmDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class FilmCrudController {

    private final FilmFacade filmFacade;

    @PostMapping("/films")
    public FilmDto createFilm(@RequestBody @Valid CreateFilmDto dto) {
        return filmFacade.createFilm(dto);
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

