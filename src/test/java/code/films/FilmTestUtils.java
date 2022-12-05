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
        return AddFilmDto
                .builder()
                .title("Sample title")
                .filmCategory(FilmCategoryDto.COMEDY)
                .year(Year.now().getValue())
                .build();
    }

    public static List<AddFilmDto> sampleAddFilmDTOs() {
        var dto1 = AddFilmDto
                .builder()
                .title("Sample title 1")
                .filmCategory(FilmCategoryDto.COMEDY)
                .year(Year.now().getValue())
                .build();
        var dto2 = AddFilmDto
                .builder()
                .title("Sample title 1")
                .filmCategory(FilmCategoryDto.COMEDY)
                .year(Year.now().getValue())
                .build();
        return List.of(dto1, dto2);
    }

    public static List<FilmDto> addSampleFilms(FilmFacade filmFacade) {
        var sampleFilmDTO1 = filmFacade.add(
                AddFilmDto
                        .builder()
                        .title("Sample title 1")
                        .filmCategory(FilmCategoryDto.COMEDY)
                        .year(Year.now().getValue())
                        .build()
        );
        var sampleFilmDTO2 = filmFacade.add(
                AddFilmDto
                        .builder()
                        .title("Sample title 2")
                        .filmCategory(FilmCategoryDto.DRAMA)
                        .year(Year.now().plusYears(1).getValue())
                        .build()
        );
        return List.of(sampleFilmDTO1, sampleFilmDTO2);
    }

    public static List<Integer> wrongFilmYears() {
        var currentYear = Year.now();
        return List.of(
                currentYear.minusYears(2).getValue(),
                currentYear.plusYears(2).getValue()
        );
    }
}
