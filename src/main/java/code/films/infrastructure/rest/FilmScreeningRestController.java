package code.films.infrastructure.rest;

import code.films.application.dto.FilmScreeningSeatDto;
import code.films.application.handlers.FilmScreeningSeatReadHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/screenings")
@RequiredArgsConstructor
public class FilmScreeningRestController {

    private final FilmScreeningSeatReadHandler screeningSeatReadHandler;

    @GetMapping("/{screeningId}/seats")
    public List<FilmScreeningSeatDto> readSeats(@PathVariable Long screeningId) {
        return screeningSeatReadHandler.handle(screeningId);
    }
}
