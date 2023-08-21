package code.catalog.infrastructure.rest;

import code.catalog.application.dto.FilmCreateDto;
import code.catalog.application.dto.FilmDto;
import code.catalog.application.services.CatalogFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
class FilmController {

    private final CatalogFacade catalogFacade;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    void createFilm(@RequestBody @Valid FilmCreateDto dto) {
        catalogFacade.createFilm(dto);
    }

    @GetMapping
    List<FilmDto> readAllFilms() {
        return catalogFacade.readAllFilms();
    }
}
