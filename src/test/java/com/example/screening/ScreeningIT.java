package com.example.screening;

import com.example.SpringTestsSpec;
import com.example.film.FilmFacade;
import com.example.screening.dto.AddScreeningRoomDTO;
import com.example.screening.exception.ScreeningRoomAlreadyExistsException;
import com.example.screening.exception.WrongScreeningYearException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.film.FilmTestUtils.addSampleDistinctFilms;
import static com.example.film.FilmTestUtils.addSampleFilm;
import static com.example.screening.ScreeningTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScreeningIT extends SpringTestsSpec {

    @Autowired
    private ScreeningFacade screeningFacade;

    @Autowired
    private FilmFacade filmFacade;

    @Test
    void should_add_screening() {
        var addedFilm = addSampleFilm(filmFacade);
        var addedScreening = screeningFacade.add(
                sampleAddScreeningDTO(addedFilm.id()),
                currentYear
        );
        assertThat(
                screeningFacade.readScreening(addedScreening.id())
        ).isEqualTo(addedScreening);
    }

    @Test
    void should_throw_exception_when_new_screening_year_is_not_current_or_previous_one() {
        var sampleFilm = addSampleFilm(filmFacade);
        assertThrows(
                WrongScreeningYearException.class,
                () -> addSampleScreeningWithWrongFilmYear(sampleFilm.id(), screeningFacade)
        );
    }

    @Test
    void should_return_all_screenings() {
        var sampleFilm = addSampleFilm(filmFacade);
        var sampleScreenings = addSampleDistinctScreenings(sampleFilm.id(), screeningFacade);
        assertThat(
                screeningFacade.readAll()
        ).isEqualTo(sampleScreenings);
    }

    @Test
    void should_return_screenings_by_film_id() {
        var sampleFilms = addSampleDistinctFilms(filmFacade);
        var sampleFilmId1 = sampleFilms.get(0).id();
        var sampleFilmId2 = sampleFilms.get(1).id();
        addSampleScreening(sampleFilmId1, screeningFacade);
        addSampleScreening(sampleFilmId2, screeningFacade);
        assertThat(
                screeningFacade.readAllByFilmId(sampleFilmId1)
        ).allMatch(
                screening -> screening.filmId().equals(sampleFilmId1)
        );
    }

    @Test
    void should_return_screenings_by_date() {
        var sampleFilm = addSampleFilm(filmFacade);
        var sampleScreenings = addSampleDistinctScreenings(sampleFilm.id(), screeningFacade);
        var sampleDate = sampleScreenings
                .get(0)
                .date();
        assertThat(
                screeningFacade.readAllByDate(ScreeningDate.of(sampleDate, currentYear))
        ).allMatch(
                screening -> screening.date().equals(sampleDate)
        );
    }

    @Test
    void should_add_screening_room() {
        var sampleDTO = AddScreeningRoomDTO
                .builder()
                .number(1)
                .freeSeats(200)
                .build();
        var screeningRoomDTO = screeningFacade.addRoom(sampleDTO);
        var addedRoom = screeningFacade.readRoom(screeningRoomDTO.uuid());
        assertThat(addedRoom.number()).isEqualTo(sampleDTO.number());
        assertThat(addedRoom.freeSeats()).isEqualTo(sampleDTO.freeSeats());
    }

    @Test
    void should_throw_exception_when_room_number_is_notUnique() {
        var sampleDTO = AddScreeningRoomDTO
                .builder()
                .number(1)
                .freeSeats(200)
                .build();
        screeningFacade.addRoom(sampleDTO);
        assertThrows(
                ScreeningRoomAlreadyExistsException.class,
                () -> screeningFacade.addRoom(sampleDTO)
        );
    }
}

