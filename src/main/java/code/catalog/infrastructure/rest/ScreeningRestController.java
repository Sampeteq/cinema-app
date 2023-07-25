package code.catalog.infrastructure.rest;

import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.application.dto.ScreeningDto;
import code.catalog.application.services.ScreeningCreateService;
import code.catalog.application.services.ScreeningReadService;
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
public class ScreeningRestController {

    private final ScreeningCreateService screeningCreateService;
    private final ScreeningReadService screeningReadService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createScreening(
            @RequestBody
            @Valid
            ScreeningCreateDto dto
    ) {
        System.out.println(dto.date());
        screeningCreateService.createScreening(dto);
    }

    @GetMapping
    public List<ScreeningDto> readAll() {
        return screeningReadService.readAllScreenings();
    }

    @GetMapping("/by/title")
    public List<ScreeningDto> readByTitle(@RequestParam String title) {
        return screeningReadService.readScreeningsByFilmTitle(title);
    }

    @GetMapping("/by/category")
    public List<ScreeningDto> readByCategory(@RequestParam FilmCategory category) {
        return screeningReadService.readScreeningsByCategory(category);
    }

    @GetMapping("/by/date")
    public List<ScreeningDto> readByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return screeningReadService.readScreeningsByDate(date);
    }
}
