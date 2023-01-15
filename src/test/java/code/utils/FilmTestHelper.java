package code.utils;

import code.films.FilmFacade;
import code.films.dto.FilmCategoryView;
import code.films.dto.FilmCreatingRequest;
import code.films.dto.FilmView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.List;

@AllArgsConstructor
@Component
public class FilmTestHelper {

    private final FilmFacade filmFacade;

    public static FilmCreatingRequest createFilmCreatingRequest() {
        return new FilmCreatingRequest(
                "title 1",
                FilmCategoryView.COMEDY,
                Year.now().getValue(),
                120
        );
    }

    public static List<FilmCreatingRequest> createFilmCreatingRequests() {
        var dto1 = new FilmCreatingRequest(
                "title 1",
                FilmCategoryView.COMEDY,
                Year.now().getValue(),
                120
        );
        var dto2 = new FilmCreatingRequest(
                "title 2",
                FilmCategoryView.DRAMA,
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

    public FilmView createFilm() {
        var filmCreatingRequest = createFilmCreatingRequest();
        return filmFacade.createFilm(filmCreatingRequest);
    }

    public List<FilmView> createFilms() {
        return createFilmCreatingRequests()
                .stream()
                .map(filmFacade::createFilm)
                .toList();
    }
}
