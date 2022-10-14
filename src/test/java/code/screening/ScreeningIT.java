package code.screening;

import code.SpringTestsSpec;
import code.film.FilmFacade;
import code.screening.dto.AddScreeningDTO;
import code.screening.dto.AddScreeningRoomDTO;
import code.screening.exception.ScreeningRoomAlreadyExistsException;
import code.screening.exception.ScreeningYearException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.Year;

import static code.film.FilmTestUtils.addSampleDistinctFilms;
import static code.film.FilmTestUtils.addSampleFilm;
import static code.screening.ScreeningTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScreeningIT extends SpringTestsSpec {

    @Autowired
    private ScreeningFacade screeningFacade;

    @Autowired
    private FilmFacade filmFacade;

    @Test
    void should_add_screening() {
        var sampleFilm = addSampleFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        var addedScreening = screeningFacade.add(
                sampleAddScreeningDTO(sampleFilm.id(), sampleRoom.uuid()),
                currentYear
        );
        assertThat(
                screeningFacade.readScreening(addedScreening.id())
        ).isEqualTo(addedScreening);
    }

    @ParameterizedTest
    @MethodSource("code.screening.ScreeningTestUtils#getWrongScreeningYears")
    void should_throw_exception_when_screening_year_is_not_current_or_next_one(Integer wrongYear) {
        var sampleFilm = addSampleFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        var currentYear= Year.now();
        assertThrows(
                ScreeningYearException.class,
                () -> screeningFacade.add(
                        AddScreeningDTO
                                .builder()
                                .date(LocalDateTime.of(wrongYear,1,1,16,30))
                                .freeSeats(200)
                                .minAge(13)
                                .filmId(sampleFilm.id())
                                .roomUuid(sampleRoom.uuid())
                                .build(),
                        currentYear
                )
        );
    }

    @Test
    void should_return_all_screenings() {
        var sampleFilm = addSampleFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        var sampleScreenings = addSampleDistinctScreenings(sampleFilm.id(), sampleRoom.uuid(), screeningFacade);
        assertThat(
                screeningFacade.readAll()
        ).isEqualTo(sampleScreenings);
    }

    @Test
    void should_return_screenings_by_film_id() {
        var sampleFilms = addSampleDistinctFilms(filmFacade);
        var sampleFilmId1 = sampleFilms.get(0).id();
        var sampleFilmId2 = sampleFilms.get(1).id();
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        addSampleScreening(sampleFilmId1, sampleRoom.uuid(), screeningFacade);
        addSampleScreening(sampleFilmId2, sampleRoom.uuid(), screeningFacade);
        assertThat(
                screeningFacade.readAllByFilmId(sampleFilmId1)
        ).allMatch(
                screening -> screening.filmId().equals(sampleFilmId1)
        );
    }

    @Test
    void should_return_screenings_by_date() {
        var sampleFilm = addSampleFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        var sampleScreenings = addSampleDistinctScreenings(sampleFilm.id(), sampleRoom.uuid(), screeningFacade);
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

