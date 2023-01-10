package code.utils;

import code.screenings.ScreeningFacade;
import code.screenings.dto.ScreeningCreatingRequest;
import code.screenings.dto.ScreeningRoomCreatingRequest;
import code.screenings.dto.ScreeningRoomView;
import code.screenings.dto.ScreeningView;
import code.screenings.dto.SeatView;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

import static code.utils.FilmTestUtils.createFilm;

public class ScreeningTestUtils {

    private static final int currentYear = Year.now().getValue();

    public static ScreeningCreatingRequest createScreeningCreatingRequest(UUID filmId, UUID roomId) {
        return new ScreeningCreatingRequest(
                LocalDateTime.of(currentYear, 5, 10, 18, 30),
                13,
                filmId,
                roomId
        );
    }

    public static ScreeningCreatingRequest createScreeningCreatingRequest(
            UUID filmId,
            UUID roomId,
            LocalDateTime screeningDate
    ) {
        return new ScreeningCreatingRequest(
                screeningDate,
                13,
                filmId,
                roomId
        );
    }

    public static ScreeningRoomCreatingRequest createRoomCreatingRequest() {
        return new ScreeningRoomCreatingRequest(
                1,
                6,
                10
        );
    }

    public static ScreeningRoomView createScreeningRoom(ScreeningFacade screeningFacade) {
        return screeningFacade.createRoom(
                createRoomCreatingRequest()
        );
    }

    public static List<ScreeningRoomView> createScreeningRooms(ScreeningFacade screeningFacade) {
        var room1 = new ScreeningRoomCreatingRequest(
                1,
                6,
                10
        );
        var room2 = new ScreeningRoomCreatingRequest(
                2,
                8,
                7
        );
        return List.of(
                screeningFacade.createRoom(room1),
                screeningFacade.createRoom(room2)
        );
    }

    public static ScreeningView createScreening(ScreeningFacade screeningFacade) {
        var sampleFilm = createFilm(screeningFacade);
        var sampleRoom = createScreeningRoom(screeningFacade);
        return screeningFacade.createScreening(
                createScreeningCreatingRequest(
                        sampleFilm.id(),
                        sampleRoom.id()
                )
        );
    }

    public static List<SeatView> searchScreeningSeats(
            UUID screeningId,
            ScreeningFacade screeningFacade
    ) {
        return screeningFacade.searchScreeningSeats(screeningId);
    }

    public static ScreeningView createScreening(
            ScreeningFacade screeningFacade,
            LocalDateTime screeningDate
    ) {
        var sampleFilm = createFilm(screeningFacade);
        var sampleRoom = createScreeningRoom(screeningFacade);
        return screeningFacade.createScreening(
                createScreeningCreatingRequest(
                        sampleFilm.id(),
                        sampleRoom.id()
                ).withDate(screeningDate)
        );
    }

    public static List<ScreeningView> createScreenings(ScreeningFacade screeningFacade) {
        var sampleFilms = FilmTestUtils.createFilms(screeningFacade);
        var sampleRooms = createScreeningRooms(screeningFacade);
        var screening1 = screeningFacade.createScreening(
                new ScreeningCreatingRequest(
                        LocalDateTime
                                .of(currentYear, 5, 5, 18, 30),
                        13,
                        sampleFilms.get(0).id(),
                        sampleRooms.get(0).id()
                )
        );
        var screening2 = screeningFacade.createScreening(
                new ScreeningCreatingRequest(
                        LocalDateTime.of(currentYear, 7, 3, 20, 30),
                        18,
                        sampleFilms.get(1).id(),
                        sampleRooms.get(1).id()
                )
        );
        return List.of(screening1, screening2);
    }

    public static List<String> getWrongScreeningDates() {
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
