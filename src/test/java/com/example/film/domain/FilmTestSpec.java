package com.example.film.domain;

import com.example.film.domain.dto.AddFilmDTO;
import com.example.film.domain.dto.FilmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FilmTestSpec {
    @Autowired
    public FilmAPI filmAPI;

    public FilmDTO addSampleFilm() {
        return filmAPI.addFilm(
                new AddFilmDTO("Sample film", FilmCategory.COMEDY, 2022)
        );
    }

    public List<FilmDTO> addSampleFilms() {
        var sampleFilm1= filmAPI.addFilm(
                new AddFilmDTO("Sample film 1", FilmCategory.COMEDY, 2022)
        );
        var sampleFilm2= filmAPI.addFilm(
                new AddFilmDTO("Sample film 2", FilmCategory.ACTION, 2021)
        );
        return List.of(sampleFilm1, sampleFilm2);
    }
}
