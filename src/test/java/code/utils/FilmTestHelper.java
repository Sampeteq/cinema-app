package code.utils;

import code.screenings.application.ScreeningFacade;
import code.screenings.domain.FilmCategory;
import code.screenings.application.dto.CreateFilmDto;
import code.screenings.application.dto.FilmDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.List;

@AllArgsConstructor
@Component
public class FilmTestHelper {

    private final ScreeningFacade screeningFacade;

    public static CreateFilmDto createCreateFilmDto() {
        return new CreateFilmDto(
                "title 1",
                FilmCategory.COMEDY,
                Year.now().getValue(),
                120
        );
    }

    public static List<CreateFilmDto> createCreateFilmDtos() {
        var dto1 = new CreateFilmDto(
                "title 1",
                FilmCategory.COMEDY,
                Year.now().getValue(),
                120
        );
        var dto2 = new CreateFilmDto(
                "title 2",
                FilmCategory.DRAMA,
                Year.now().getValue() - 1,
                90
        );
        return List.of(dto1, dto2);
    }

    public static List<Integer> getWrongFilmYears() {
        var currentYear = Year.now();
        return List.of(
                currentYear.minusYears(2).getValue(),
                currentYear.plusYears(2).getValue()
        );
    }

    public FilmDto createFilm() {
        var filmCreatingRequest = createCreateFilmDto();
        return screeningFacade.createFilm(filmCreatingRequest);
    }

    public List<FilmDto> createFilms() {
        return createCreateFilmDtos()
                .stream()
                .map(screeningFacade::createFilm)
                .toList();
    }
}