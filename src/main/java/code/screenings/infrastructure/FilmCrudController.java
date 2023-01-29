package code.screenings.infrastructure;

import code.screenings.application.FilmSearchParams;
import code.screenings.application.ScreeningFacade;
import code.screenings.domain.FilmCategory;
import code.screenings.application.dto.CreateFilmDto;
import code.screenings.application.dto.FilmDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class FilmCrudController {

    private final ScreeningFacade screeningFacade;

    @PostMapping("/films")
    public FilmDto createFilm(
            @RequestBody
            @Valid
            CreateFilmDto dto
    ) {
        return screeningFacade.createFilm(dto);
    }

    @GetMapping("/films")
    public List<FilmDto> searchFilmsBy(
            @RequestParam(required = false)
            FilmCategory category
    ) {
        var params = FilmSearchParams
                .builder()
                .category(category)
                .build();
        return screeningFacade.searchFilmsBy(params);
    }
}

