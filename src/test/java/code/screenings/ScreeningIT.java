package code.screenings;

import code.SpringTestsSpec;
import code.films.FilmFacade;
import code.films.dto.FilmDTO;
import code.screenings.dto.AddScreeningDTO;
import code.screenings.dto.AddScreeningRoomDTO;
import code.screenings.dto.ScreeningDTO;
import code.screenings.dto.ScreeningRoomDTO;
import code.screenings.exception.ScreeningFreeSeatsQuantityBiggerThanRoomOneException;
import code.screenings.exception.ScreeningRoomAlreadyExistsException;
import code.screenings.exception.ScreeningRoomBusyException;
import code.screenings.exception.ScreeningYearException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @MethodSource("code.screenings.ScreeningTestUtils#getWrongScreeningYears")
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
    void should_throw_exception_when_screening_free_seats_quantity_is_bigger_than_room_one() {
        assertThrows(
                ScreeningFreeSeatsQuantityBiggerThanRoomOneException.class,
                () -> screeningFacade.add(
                        AddScreeningDTO
                                .builder()
                                .freeSeatsQuantity(sampleRooms.get(0).freeSeats() + 1)
                                .roomUuid(sampleRooms.get(0).uuid())
                                .date(LocalDateTime.ofInstant(clock.instant(), ZoneOffset.UTC))
                                .filmId(sampleFilms.get(0).id())
                                .minAge(13)
                                .build(),
                        currentYear
                )
        );
    }

    @Test
    void should_search_all_screenings() {
        assertThat(
                screeningFacade.searchBy(Map.of())
        ).isEqualTo(sampleScreenings);
    }

    @Test
    void should_search_screenings_by_search_params() {
        var readParams = new HashMap<String, Object>() {{
            put("filmId", sampleFilms.get(0).id());
            put("date", sampleScreenings.get(0).date());
        }};
        assertThat(
                screeningFacade.searchBy(readParams)
        ).allMatch(
                screening -> screening.filmId().equals(sampleFilms.get(0).id())
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

