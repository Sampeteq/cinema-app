package code.catalog.application.services;

import code.catalog.application.dto.FilmCreateDto;
import code.catalog.application.dto.FilmDto;
import code.catalog.application.dto.RoomCreateDto;
import code.catalog.application.dto.ScreeningCreateDto;
import code.catalog.application.dto.ScreeningDetailsDto;
import code.catalog.application.dto.ScreeningDto;
import code.catalog.domain.FilmCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CatalogFacade {

    private final FilmCreateService filmCreateService;
    private final FilmReadService filmReadService;
    private final ScreeningCreateService screeningCreateService;
    private final RoomCreateService roomCreateService;
    private final ScreeningReadService screeningReadService;

    public void createFilm(FilmCreateDto dto) {
        filmCreateService.creteFilm(dto);
    }

    public List<FilmDto> readAllFilms() {
        return filmReadService.readAll();
    }

    public void createScreening(ScreeningCreateDto dto) {
        screeningCreateService.createScreening(dto);
    }

    public void createRoom(RoomCreateDto dto) {
        roomCreateService.createRoom(dto);
    }

    public ScreeningDetailsDto readScreeningDetails(Long screeningId) {
        return screeningReadService.readScreeningDetails(screeningId);
    }

    public List<ScreeningDto> readAllScreenings() {
        return screeningReadService.readAllScreenings();
    }

    public List<ScreeningDto> readScreeningsByFilmTitle(String title) {
        return screeningReadService.readScreeningsByFilmTitle(title);
    }

    public List<ScreeningDto> readScreeningsByCategory(FilmCategory category) {
        return screeningReadService.readScreeningsByCategory(category);
    }

    public List<ScreeningDto> readScreeningsByDate(LocalDate date) {
        return screeningReadService.readScreeningsByDate(date);
    }
}
