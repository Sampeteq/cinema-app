package code.film;

import code.film.dto.AddFilmDTO;
import code.film.dto.FilmDTO;

import java.time.Year;
import java.util.List;

public class FilmTestUtils {

    private static final Year currentYear = Year.now();

    public static AddFilmDTO addFilmDTO() {
        return AddFilmDTO
                .builder()
                .title("Sample title")
                .filmCategory(FilmCategory.COMEDY)
                .year(Year.now().getValue())
                .build();
    }

    public static AddFilmDTO addFilmDTOWithWrongYear(int wrongFilmYear) {
        return AddFilmDTO
                .builder()
                .title("Sample title")
                .filmCategory(FilmCategory.COMEDY)
                .year(wrongFilmYear)
                .build();
    }

    public static FilmDTO addFilm(FilmFacade filmFacade) {
        return filmFacade.add(
                AddFilmDTO
                        .builder()
                        .title("Sample title")
                        .filmCategory(FilmCategory.COMEDY)
                        .year(Year.now().getValue())
                        .build()
        );
    }

    public static List<FilmDTO> addDistinctFilms(FilmFacade filmFacade) {
        var sampleFilmDTO1 = filmFacade.add(
                AddFilmDTO
                        .builder()
                        .title("Sample title 1")
                        .filmCategory(FilmCategory.COMEDY)
                        .year(Year.now().getValue())
                        .build()
        );
        var sampleFilmDTO2 = filmFacade.add(
                AddFilmDTO
                        .builder()
                        .title("Sample title 2")
                        .filmCategory(FilmCategory.DRAMA)
                        .year(Year.now().plusYears(1).getValue())
                        .build()
        );
        return List.of(sampleFilmDTO1, sampleFilmDTO2);
    }

    public static List<Integer> wrongFilmYears() {
        return List.of(
                currentYear.minusYears(2).getValue(),
                currentYear.plusYears(2).getValue()
        );
    }
}
