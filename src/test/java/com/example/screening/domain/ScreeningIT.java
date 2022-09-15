package com.example.screening.domain;

import com.example.screening.domain.exception.WrongScreeningYearException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScreeningIT extends ScreeningTestSpec {

    @Test
    void should_add_screening() {
        var addedFilm = addSampleFilm();
        var addedScreening = screeningAPI.addScreening(
                sampleAddScreeningDTO(addedFilm.filmId()),
                currentYear
        );
        assertThat(
                screeningAPI.readScreeningById(addedScreening.id())
        ).isEqualTo(addedScreening);
    }

    @Test
    void should_throw_exception_when_new_screening_year_is_not_current_or_previous_one() {
        var addedFilm = addSampleFilm();
        assertThrows(
                WrongScreeningYearException.class,
                () -> screeningAPI.addScreening(
                        sampleAddScreeningDTOwithWrongFilmYear(addedFilm),
                        currentYear
                )
        );
    }

    @Test
    void should_return_all_screenings() {
        var sampleFilm = addSampleFilm();
        var sampleScreenings = addSampleScreenings(sampleFilm.filmId());
        assertThat(
                screeningAPI.readAllScreenings()
        ).isEqualTo(sampleScreenings);
    }

    @Test
    void should_return_screenings_by_film_id() {
        var sampleFilms = addSampleFilms();
        var sampleFilmId1 = sampleFilms.get(0).filmId();
        var sampleFilmId2 = sampleFilms.get(1).filmId();
        addSampleScreening(sampleFilmId1);
        addSampleScreening(sampleFilmId2);
        assertThat(
                screeningAPI.readScreeningsByFilmId(sampleFilmId1)
        ).allMatch(
                screening -> screening.filmId().equals(sampleFilmId1)
        );
    }

    @Test
    void should_return_screenings_by_date() {
        var sampleFilm = addSampleFilm();
        var sampleScreenings = addSampleScreenings(sampleFilm.filmId());
        var sampleDate = sampleScreenings
                .get(0)
                .date();
        assertThat(
                screeningAPI.readAllScreeningsByDate(ScreeningDate.of(sampleDate, currentYear))
        ).allMatch(
                screening -> screening.date().equals(sampleDate)
        );
    }
}
