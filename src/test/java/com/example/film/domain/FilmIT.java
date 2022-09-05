package com.example.film.domain;

import com.example.film.domain.dto.AddFilmDTO;
import com.example.film.domain.dto.FilmDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class FilmIT {
    @Autowired
    private FilmAPI filmAPI;

    @Test
    void should_add_film() {
        var addedFilm= filmAPI.addFilm(sampleAddFilmDto() );
        assertThat(filmAPI.readFilmById(addedFilm.filmId() ) ).isEqualTo(addedFilm);
    }

    @Test
    void should_return_all_films() {
        var sampleFilms= addSampleFilms();
        assertThat(filmAPI.readAllFilms() ).isEqualTo(sampleFilms);
    }

    @Test
    void should_return_films_with_given_category() {
        addSampleFilms();
        assertThat(
                filmAPI.readFilmsByCategory(FilmCategory.COMEDY )
        ).allMatch(
                film -> film.category().equals(FilmCategory.COMEDY)
        );
    }

    private AddFilmDTO sampleAddFilmDto() {
        return new AddFilmDTO("Sample film", FilmCategory.COMEDY, 2022);
    }

    private List<FilmDTO> addSampleFilms() {
        return List.of(
                filmAPI.addFilm(new AddFilmDTO("Sample film 1", FilmCategory.COMEDY, 2022)),
                filmAPI.addFilm(new AddFilmDTO("Sample film 2", FilmCategory.ACTION, 2021))
        );
    }
}
