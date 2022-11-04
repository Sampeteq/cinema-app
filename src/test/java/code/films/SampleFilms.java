package code.films;

import code.films.dto.AddFilmDTO;
import code.films.dto.FilmDTO;

import java.time.Year;
import java.util.List;

public interface SampleFilms {

    default AddFilmDTO sampleAddFilmDTO() {
        return AddFilmDTO
                .builder()
                .title("Sample title")
                .filmCategory(FilmCategory.COMEDY)
                .year(Year.now().getValue())
                .build();
    }

    default List<AddFilmDTO> sampleAddFilmDTOs() {
        var dto1 = AddFilmDTO
                .builder()
                .title("Sample title 1")
                .filmCategory(FilmCategory.COMEDY)
                .year(Year.now().getValue())
                .build();
        var dto2 = AddFilmDTO
                .builder()
                .title("Sample title 1")
                .filmCategory(FilmCategory.COMEDY)
                .year(Year.now().getValue())
                .build();
        return List.of(dto1, dto2);
    }

    default AddFilmDTO sampleAddFilmDTOWithWrongFilmYear(int wrongFilmYear) {
        return AddFilmDTO
                .builder()
                .title("Sample title")
                .filmCategory(FilmCategory.COMEDY)
                .year(wrongFilmYear)
                .build();
    }

    static List<FilmDTO> addSampleFilms(FilmFacade filmFacade) {
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

    static List<Integer> wrongFilmYears() {
        var currentYear = Year.now();
        return List.of(
                currentYear.minusYears(2).getValue(),
                currentYear.plusYears(2).getValue()
        );
    }
}
