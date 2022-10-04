package com.example.film.domain;

import com.example.film.domain.dto.AddFilmDTO;
import com.example.film.domain.dto.FilmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

@SpringBootTest
@Transactional
public class FilmTestSpec {
    @Autowired
    public FilmFacade filmFacade;

    public AddFilmDTO sampleAddFilmDto() {
        return new AddFilmDTO("Sample film", FilmCategory.COMEDY, 2022);
    }

    public AddFilmDTO sampleAddFilmDtoWithWrongFilmYear() {
        return new AddFilmDTO("Sample film", FilmCategory.COMEDY, Year.now().getValue() - 5);
    }

    public FilmDTO addSampleFilm() {
        return filmFacade.addFilm(
                new AddFilmDTO("Sample film", FilmCategory.COMEDY, 2022)
        );
    }

    public List<FilmDTO> addSampleFilms() {
        var sampleFilm1 = filmFacade.addFilm(
                new AddFilmDTO("Sample film 1", FilmCategory.COMEDY, 2022)
        );
        var sampleFilm2 = filmFacade.addFilm(
                new AddFilmDTO("Sample film 2", FilmCategory.ACTION, 2021)
        );
        return List.of(sampleFilm1, sampleFilm2);
    }
}
