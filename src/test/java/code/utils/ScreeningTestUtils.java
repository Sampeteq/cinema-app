package code.utils;

import code.screenings.ScreeningFacade;
import code.screenings.dto.*;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

import static code.utils.FilmTestUtils.createSampleFilm;

public class ScreeningTestUtils {

    private static final int currentYear = Year.now().getValue();

    public static CreateScreeningDto sampleCreateScreeningDto(UUID filmId, UUID roomId) {
        return new CreateScreeningDto(
                LocalDateTime.of(currentYear, 5, 10, 18, 30),
                13,
                filmId,
                roomId
        );
    }

    public static CreateScreeningDto sampleCreateScreeningDto(
            UUID filmId,
            UUID roomId,
            LocalDateTime screeningDate
    ) {
        return new CreateScreeningDto(
                screeningDate,
                13,
                filmId,
                roomId
        );
    }

    public static CreateScreeningRoomDto sampleCreateRoomDto() {
        return new CreateScreeningRoomDto(
                1,
                6,
                10
        );
    }

    public static ScreeningRoomDto createSampleScreeningRoom(ScreeningFacade screeningFacade) {
        return screeningFacade.createRoom(
                sampleCreateRoomDto()
        );
    }

    public static List<ScreeningRoomDto> createSampleScreeningRooms(ScreeningFacade screeningFacade) {
        var room1 = new CreateScreeningRoomDto(
                1,
                6,
                10
        );
        var room2 = new CreateScreeningRoomDto(
                2,
                8,
                7
        );
        return List.of(
                screeningFacade.createRoom(room1),
                screeningFacade.createRoom(room2)
        );
    }

    public static ScreeningDto createSampleScreening(ScreeningFacade screeningFacade) {
        var sampleFilm = createSampleFilm(screeningFacade);
        var sampleRoom = createSampleScreeningRoom(screeningFacade);
        return screeningFacade.createScreening(
                sampleCreateScreeningDto(
                        sampleFilm.id(),
                        sampleRoom.id()
                )
        );
    }

    public static List<SeatDto> searchSampleScreeningSeats(
            UUID screeningId,
            ScreeningFacade screeningFacade
    ) {
        return screeningFacade.searchScreeningSeats(screeningId);
    }

    public static ScreeningDto createSampleScreening(
            ScreeningFacade screeningFacade,
            LocalDateTime screeningDate
    ) {
        var sampleFilm = createSampleFilm(screeningFacade);
        var sampleRoom = createSampleScreeningRoom(screeningFacade);
        return screeningFacade.createScreening(
                sampleCreateScreeningDto(
                        sampleFilm.id(),
                        sampleRoom.id()
                ).withDate(screeningDate)
        );
    }

    public static List<ScreeningDto> createSampleScreenings(ScreeningFacade screeningFacade) {
        var sampleFilms = FilmTestUtils.createSampleFilms(screeningFacade);
        var sampleRooms = createSampleScreeningRooms(screeningFacade);
        var screening1 = screeningFacade.createScreening(
                new CreateScreeningDto(
                        LocalDateTime
                                .of(currentYear, 5, 5, 18, 30),
                        13,
                        sampleFilms.get(0).id(),
                        sampleRooms.get(0).id()
                )
        );
        var screening2 = screeningFacade.createScreening(
                new CreateScreeningDto(
                        LocalDateTime.of(currentYear, 7, 3, 20, 30),
                        18,
                        sampleFilms.get(1).id(),
                        sampleRooms.get(1).id()
                )
        );
        return List.of(screening1, screening2);
    }

    public static List<String> wrongScreeningDates() {
        var date1 = LocalDateTime.of(
                currentYear - 1,
                2,
                2,
                16,
                30
        ).toString();
        var date2 = LocalDateTime.of(
                currentYear + 2,
                2,
                2,
                16,
                30
        ).toString();
        return List.of(date1, date2);
    }

}
