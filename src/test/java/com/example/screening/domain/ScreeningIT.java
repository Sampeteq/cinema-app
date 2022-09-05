package com.example.screening.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ScreeningIT extends ScreeningTestSpec {

    @Test
    void should_add_screening() {
        var addedFilm = addSampleFilm();
        var addedScreening= screeningAPI.addScreening(
                sampleAddScreeningDTO(addedFilm)
        );
        assertThat(screeningAPI.readScreeningById(addedScreening.id() ) ).isEqualTo(addedScreening);
    }

    @Test
    void should_return_all_screenings() {
        var sampleFilms= addSampleFilms();
        var sampleFilmId1= sampleFilms.get(0).filmId();
        var sampleFilmId2= sampleFilms.get(1).filmId();
        var sampleScreenings= addSampleScreenings(sampleFilmId1, sampleFilmId2 );
        assertThat(screeningAPI.readAllScreenings() ).isEqualTo(sampleScreenings);
    }

    @Test
    void should_return_screenings_by_film_id() {
        var sampleFilms= addSampleFilms();
        var sampleFilmId1= sampleFilms.get(0).filmId();
        var sampleFilmId2= sampleFilms.get(1).filmId();
        addSampleScreenings(sampleFilmId1, sampleFilmId2);
        assertThat(
                screeningAPI.readScreeningsByFilmId(sampleFilmId1 )
        ).allMatch(
                screening -> screening.filmId().equals(sampleFilmId1 )
        );
    }

    @Test
    void should_return_screenings_by_date() {
        var sampleFilms= addSampleFilms();
        var sampleFilmId1= sampleFilms.get(0).filmId();
        var sampleFilmId2= sampleFilms.get(1).filmId();
        var sampleScreenings= addSampleScreenings(sampleFilmId1, sampleFilmId2);
        var date = sampleScreenings.get(0).date();
        assertThat(
                screeningAPI.readAllScreeningsByDate(date)
        ).allMatch(
                screening -> screening.filmId().equals(sampleFilmId1 )
        );
    }
}
