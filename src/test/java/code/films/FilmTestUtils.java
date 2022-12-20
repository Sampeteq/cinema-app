package code.films;

import code.films.dto.AddFilmDto;
import code.films.dto.FilmCategoryDto;
import code.films.dto.FilmDto;

import java.time.Year;
import java.util.List;

public abstract class FilmTestUtils {

    private FilmTestUtils() {
    }

    public static AddFilmDto sampleAddFilmDTO() {
        return new AddFilmDto(
                "title 1",
                FilmCategoryDto.COMEDY,
                Year.now().getValue()
        );
    }

    public static List<AddFilmDto> sampleAddFilmDTOs() {
        var dto1 = new AddFilmDto(
                "title 1",
                FilmCategoryDto.COMEDY,
                Year.now().getValue()
        );
        var dto2 = new AddFilmDto(
                "title 2",
                FilmCategoryDto.DRAMA,
                Year.now().getValue() - 1
        );
        return List.of(dto1, dto2);
    }

    public static List<FilmDto> addSampleFilms(FilmFacade filmFacade) {
        return sampleAddFilmDTOs()
                .stream()
                .map(filmFacade::add)
                .toList();
    }

    public static List<Integer> wrongFilmYears() {
        var currentYear = Year.now();
        return List.of(
                currentYear.minusYears(2).getValue(),
                currentYear.plusYears(2).getValue()
        );
    }
}
