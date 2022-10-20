package code.screenings;

import code.SpringTestsSpec;
import code.films.FilmFacade;
import code.films.dto.FilmDTO;
import code.screenings.dto.AddScreeningDTO;
import code.screenings.dto.AddScreeningRoomDTO;
import code.screenings.dto.ScreeningDTO;
import code.screenings.dto.ScreeningRoomDTO;
import code.screenings.exception.ScreeningRoomAlreadyExistsException;
import code.screenings.exception.ScreeningRoomBusyException;
import code.screenings.exception.ScreeningYearException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.util.List;

import static code.films.FilmTestUtils.addSampleFilms;
import static code.screenings.ScreeningTestUtils.*;
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

    private final Clock clock = Clock.systemUTC();

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
    void should_throw_exception_when_screening_room_is_busy() {
        var sampleAddScreeningDTOWithSameDateAndRoom = AddScreeningDTO
                .builder()
                .date(sampleScreenings.get(0).date())
                .roomUuid(sampleRooms.get(0).uuid())
                .filmId(sampleFilms.get(0).id())
                .minAge(13)
                .build();
        assertThrows(
                ScreeningRoomBusyException.class,
                () -> screeningFacade.add(sampleAddScreeningDTOWithSameDateAndRoom, currentYear)
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

