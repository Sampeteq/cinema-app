package code.screenings.infrastructure;

import code.screenings.application.ScreeningFacade;
import code.screenings.domain.dto.CreateFilmDto;
import code.screenings.domain.dto.FilmCategoryDto;
import code.screenings.domain.dto.FilmDto;
import code.screenings.domain.dto.FilmSearchParamsDto;
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
            FilmCategoryDto category
    ) {
        var params = FilmSearchParamsDto
                .builder()
                .category(category)
                .build();
        return screeningFacade.searchFilmsBy(params);
    }
}

