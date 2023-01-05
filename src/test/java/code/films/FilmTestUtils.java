package code.films;

import code.films.dto.CreateFilmDto;
import code.films.dto.FilmCategoryDto;
import code.films.dto.FilmDto;

import java.time.Year;
import java.util.List;

public abstract class FilmTestUtils {

    private FilmTestUtils() {
    }

    public static CreateFilmDto sampleCreateFilmDto() {
        return new CreateFilmDto(
                "title 1",
                FilmCategoryDto.COMEDY,
                Year.now().getValue(),
                120
        );
    }

    public static List<CreateFilmDto> sampleCreateFilmDtos() {
        var dto1 = new CreateFilmDto(
                "title 1",
                FilmCategoryDto.COMEDY,
                Year.now().getValue(),
                120
        );
        var dto2 = new CreateFilmDto(
                "title 2",
                FilmCategoryDto.DRAMA,
                Year.now().getValue() - 1,
                90
        );
        return List.of(dto1, dto2);
    }

    public static FilmDto createSampleFilm(FilmFacade filmFacade) {
        return filmFacade.createFilm(
                sampleCreateFilmDto()
        );
    }

    public static List<FilmDto> createSampleFilms(FilmFacade filmFacade) {
        return sampleCreateFilmDtos()
                .stream()
                .map(filmFacade::createFilm)
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
