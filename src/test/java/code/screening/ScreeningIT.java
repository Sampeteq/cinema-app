package code.screening;

import code.SpringTestsSpec;
import code.film.FilmFacade;
import code.film.dto.FilmDTO;
import code.screening.dto.AddScreeningRoomDTO;
import code.screening.dto.ScreeningDTO;
import code.screening.dto.ScreeningRoomDTO;
import code.screening.exception.ScreeningRoomAlreadyExistsException;
import code.screening.exception.ScreeningYearException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static code.film.FilmTestUtils.addSampleFilms;
import static code.screening.ScreeningTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScreeningIT extends SpringTestsSpec {

    @Autowired
    private ScreeningFacade screeningFacade;

    @Autowired
    private FilmFacade filmFacade;

    private List<FilmDTO> sampleFilms;

    private List<ScreeningDTO> sampleScreenings;

    private List<ScreeningRoomDTO> sampleRooms;

    @BeforeEach
    void initTestData() {
        sampleFilms = addSampleFilms(filmFacade);
        sampleRooms = addSampleScreeningRooms(screeningFacade);
        sampleScreenings = addSampleScreenings(sampleFilms, sampleRooms, screeningFacade);
    }

    @Test
    void should_add_screening() {
        var addedScreening = screeningFacade.add(
                sampleAddScreeningDTO(sampleFilms.get(0).id(), sampleRooms.get(0).uuid()),
                currentYear
        );
        assertThat(
                screeningFacade.read(addedScreening.id())
        ).isEqualTo(addedScreening);
    }

    @ParameterizedTest
    @MethodSource("code.screening.ScreeningTestUtils#getWrongScreeningYears")
    void should_throw_exception_when_screening_year_is_not_current_or_next_one(Integer wrongScreeningYear) {
        assertThrows(
                ScreeningYearException.class,
                () -> screeningFacade.add(
                        sampleAddScreeningDTOWithWrongScreeningYear(
                                sampleFilms.get(0).id(),
                                sampleRooms.get(0).uuid(),
                                wrongScreeningYear
                        ),
                        currentYear
                )
        );
    }

    @Test
    void should_return_all_screenings() {
        assertThat(
                screeningFacade.readAll()
        ).isEqualTo(sampleScreenings);
    }

    @Test
    void should_return_screenings_by_film_id() {
        assertThat(
                screeningFacade.readByFilmId(sampleFilms.get(0).id())
        ).allMatch(
                screening -> screening.filmId().equals(sampleFilms.get(0).id())
        );
    }

    @Test
    void should_return_screenings_by_date() {
        assertThat(
                screeningFacade.readByDate(sampleScreenings.get(0).date(), currentYear)
        ).allMatch(
                screening -> screening.date().equals(sampleScreenings.get(0).date())
        );
    }

    @Test
    void should_add_screening_room() {
        var addedScreeningRoom = screeningFacade.addRoom(
                AddScreeningRoomDTO
                        .builder()
                        .number(5)
                        .freeSeats(200)
                        .build()
        );
        assertThat(
                screeningFacade.readRoom(addedScreeningRoom.uuid())
        ).isEqualTo(addedScreeningRoom);
    }

    @Test
    void should_throw_exception_when_room_number_is_not_unique() {
        assertThrows(
                ScreeningRoomAlreadyExistsException.class,
                () -> screeningFacade.addRoom(
                        AddScreeningRoomDTO
                                .builder()
                                .number(sampleRooms.get(0).number())
                                .freeSeats(200)
                                .build()
                )
        );
    }
}

