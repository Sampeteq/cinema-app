package com.example.film;

import com.example.film.dto.AddFilmDTO;
import com.example.film.dto.FilmDTO;

import java.time.Year;
import java.util.List;

public class FilmTestUtils {

    public static AddFilmDTO sampleAddFilmDTO() {
        return new AddFilmDTO("Sample film", FilmCategory.COMEDY, 2022);
    }

    public static AddFilmDTO sampleAddFilmDTOWithWrongFilmYear() {
        return new AddFilmDTO("Sample film", FilmCategory.COMEDY, Year.now().getValue() - 3);
    }

    public static FilmDTO addSampleFilm(FilmFacade filmFacade) {
        return filmFacade.add(
                new AddFilmDTO("Sample film 1", FilmCategory.COMEDY, 2022)
        );
    }

    public static List<FilmDTO> addSampleDistinctFilms(FilmFacade filmFacade) {
        var sampleFilmDTO1 = filmFacade.add(
                new AddFilmDTO("Sample film 1", FilmCategory.COMEDY, 2022)
        );
        var sampleFilmDTO2 = filmFacade.add(
                new AddFilmDTO("Sample film 2", FilmCategory.ACTION, 2021)
        );
        return List.of(sampleFilmDTO1, sampleFilmDTO2);
    }
}
