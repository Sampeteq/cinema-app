package code.catalog.infrastructure.rest;

import code.catalog.application.dto.FilmDto;
import code.catalog.application.dto.ScreeningDto;
import code.catalog.application.dto.SeatDto;
import code.catalog.application.services.FilmReadService;
import code.catalog.application.services.SeatReadService;
import code.catalog.domain.FilmCategory;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmPublicRestController {

    private final FilmReadService filmReadService;

    private final SeatReadService screeningSeatReadService;

    @GetMapping
    public List<FilmDto> readAll() {
        return filmReadService.readAll();
    }

    @GetMapping("/screenings")
    public List<ScreeningDto> readAllScreenings() {
        return filmReadService.readAllScreenings();
    }

    @GetMapping("/screenings/by/title")
    public List<ScreeningDto> readByTitle(@RequestParam String title) {
        return filmReadService.readScreeningsByFilmTitle(title);
    }

    @GetMapping("/screenings/by/category")
    public List<ScreeningDto> readByCategory(@RequestParam FilmCategory category) {
        return filmReadService.readScreeningsByCategory(category);
    }

    @GetMapping("/screenings/by/date")
    public List<ScreeningDto> readByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return filmReadService.readScreeningsByDate(date);
    }

    @GetMapping("/screenings/{screeningId}/seats")
    public List<SeatDto> readSeats(@PathVariable Long screeningId) {
        return screeningSeatReadService.readByScreeningId(screeningId);
    }
}

