package code.catalog.infrastructure.rest;

import code.catalog.application.dto.FilmCreateDto;
import code.catalog.application.dto.FilmDto;
import code.catalog.application.services.FilmCreateService;
import code.catalog.application.services.FilmReadService;
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
public class FilmRestController {

    private final FilmCreateService filmCreateService;
    private final FilmReadService filmReadService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createFilm(@RequestBody @Valid FilmCreateDto dto) {
        filmCreateService.creteFilm(dto);
    }

    @GetMapping
    public List<FilmDto> readAll() {
        return filmReadService.readAll();
    }
}
