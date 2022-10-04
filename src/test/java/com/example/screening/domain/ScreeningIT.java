package com.example.screening.domain;

import com.example.screening.domain.exception.WrongScreeningYearException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScreeningIT extends ScreeningTestSpec {

    @Test
    void should_add_screening() {
        var addedFilm = addSampleFilm();
        var addedScreening = screeningFacade.addScreening(
                sampleAddScreeningDTO(addedFilm.id()),
                currentYear
        );
        assertThat(
                screeningFacade.readScreeningById(addedScreening.id())
        ).isEqualTo(addedScreening);
    }

    @Test
    void should_throw_exception_when_new_screening_year_is_not_current_or_previous_one() {
        var addedFilm = addSampleFilm();
        assertThrows(
                WrongScreeningYearException.class,
                () -> screeningFacade.addScreening(
                        sampleAddScreeningDTOwithWrongFilmYear(addedFilm),
                        currentYear
                )
        );
    }

    @Test
    void should_return_all_screenings() {
        var sampleFilm = addSampleFilm();
        var sampleScreenings = addSampleScreenings(sampleFilm.id());
        assertThat(
                screeningFacade.readAllScreenings()
        ).isEqualTo(sampleScreenings);
    }

    @Test
    void should_return_screenings_by_film_id() {
        var sampleFilms = addSampleFilms();
        var sampleFilmId1 = sampleFilms.get(0).id();
        var sampleFilmId2 = sampleFilms.get(1).id();
        addSampleScreening(sampleFilmId1);
        addSampleScreening(sampleFilmId2);
        assertThat(
                screeningFacade.readScreeningsByFilmId(sampleFilmId1)
        ).allMatch(
                screening -> screening.filmId().equals(sampleFilmId1)
        );
    }

    @Test
    void should_return_screenings_by_date() {
        var sampleFilm = addSampleFilm();
        var sampleScreenings = addSampleScreenings(sampleFilm.id());
        var sampleDate = sampleScreenings
                .get(0)
                .date();
        assertThat(
                screeningFacade.readAllScreeningsByDate(ScreeningDate.of(sampleDate, currentYear))
        ).allMatch(
                screening -> screening.date().equals(sampleDate)
        );
    }
}
