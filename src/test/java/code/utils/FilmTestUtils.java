package code.utils;

import code.films.FilmFacade;
import code.films.dto.FilmCategoryView;
import code.films.dto.FilmCreatingRequest;
import code.films.dto.FilmView;

import java.time.Year;
import java.util.List;

public abstract class FilmTestUtils {

    private FilmTestUtils() {
    }

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

    public static FilmView createFilm(FilmFacade filmFacade) {
        var filmCreatingRequest = createFilmCreatingRequest();
        return filmFacade.createFilm(filmCreatingRequest);
    }

    public static List<FilmView> createFilms(FilmFacade filmFacade) {
        return createFilmCreatingRequests()
                .stream()
                .map(filmFacade::createFilm)
                .toList();
    }

    public static List<Integer> getWrongFilmYears() {
        var currentYear = Year.now();
        return List.of(
                currentYear.minusYears(2).getValue(),
                currentYear.plusYears(2).getValue()
        );
    }
}
