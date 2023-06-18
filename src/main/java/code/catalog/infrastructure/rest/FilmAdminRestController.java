package code.catalog.infrastructure.rest;

import code.catalog.application.dto.FilmCreateDto;
import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.application.services.FilmCreateService;
import code.catalog.application.services.ScreeningCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmAdminRestController {

    private final FilmCreateService filmCreateService;

    private final ScreeningCreateService screeningCreateHandler;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createFilm(@RequestBody @Valid FilmCreateDto dto) {
        filmCreateService.creteFilm(dto);
    }

    @PostMapping("/{filmId}/screenings")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createScreening(
            @PathVariable
            Long filmId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime screeningDate
    ) {
        var dto = ScreeningCreateDto
                .builder()
                .filmId(filmId)
                .date(screeningDate)
                .build();
        screeningCreateHandler.createScreening(dto);
    }
}
