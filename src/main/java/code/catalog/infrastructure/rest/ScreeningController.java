package code.catalog.infrastructure.rest;

import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.application.dto.ScreeningDto;
import code.catalog.application.services.CatalogFacade;
import code.catalog.domain.FilmCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/screenings")
@RequiredArgsConstructor
class ScreeningController {

    private final CatalogFacade catalogFacade;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    void createScreening(
            @RequestBody
            @Valid
            ScreeningCreateDto dto
    ) {
        catalogFacade.createScreening(dto);
    }

    @GetMapping
    List<ScreeningDto> readAllScreenings() {
        return catalogFacade.readAllScreenings();
    }

    @GetMapping("/by/title")
    List<ScreeningDto> readScreeningsByFilmTitle(@RequestParam String title) {
        return catalogFacade.readScreeningsByFilmTitle(title);
    }

    @GetMapping("/by/category")
    List<ScreeningDto> readScreeningsByFilmCategory(@RequestParam FilmCategory category) {
        return catalogFacade.readScreeningsByFilmCategory(category);
    }

    @GetMapping("/by/date")
    List<ScreeningDto> readByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return catalogFacade.readScreeningsByDate(date);
    }
}
