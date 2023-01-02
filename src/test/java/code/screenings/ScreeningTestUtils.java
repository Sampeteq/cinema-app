package code.screenings;

import code.films.FilmFacade;
import code.films.FilmTestUtils;
import code.screenings.dto.AddScreeningDto;
import code.screenings.dto.AddScreeningRoomDto;
import code.screenings.dto.ScreeningDto;
import code.screenings.dto.ScreeningRoomDto;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

import static code.films.FilmTestUtils.addSampleFilm;

public class ScreeningTestUtils {

    private static final int currentYear = Year.now().getValue();

    public static AddScreeningDto sampleAddScreeningDto(UUID filmId, UUID roomId) {
        return new AddScreeningDto(
                LocalDateTime.of(currentYear, 5, 10, 18, 30),
                13,
                filmId,
                roomId
        );
    }

    public static AddScreeningRoomDto sampleAddRoomDTO() {
        return new AddScreeningRoomDto(
                1,
                6,
                10
        );
    }

    public static List<ScreeningRoomDto> addSampleScreeningRooms(ScreeningFacade screeningFacade) {
        var room1 = new AddScreeningRoomDto(
                1,
                6,
                10
        );
        var room2 = new AddScreeningRoomDto(
                2,
                8,
                7
        );
        return List.of(
                screeningFacade.addRoom(room1),
                screeningFacade.addRoom(room2)
        );
    }

    public static ScreeningDto addSampleScreening(FilmFacade filmFacade, ScreeningFacade screeningFacade) {
        var sampleFilm = addSampleFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        return screeningFacade.add(
                sampleAddScreeningDto(
                        sampleFilm.id(),
                        sampleRoom.id()
                )
        );
    }

    public static ScreeningDto addSampleScreening(
            FilmFacade filmFacade,
            ScreeningFacade screeningFacade,
            LocalDateTime screeningDate
    ) {
        var sampleFilm = addSampleFilm(filmFacade);
        var sampleRoom = addSampleScreeningRoom(screeningFacade);
        return screeningFacade.add(
                sampleAddScreeningDto(
                        sampleFilm.id(),
                        sampleRoom.id()
                ).withDate(screeningDate)
        );
    }

    static ScreeningRoomDto addSampleScreeningRoom(ScreeningFacade screeningFacade) {
        return screeningFacade.addRoom(
                sampleAddRoomDTO()
        );
    }

    public static List<ScreeningDto> addSampleScreenings(ScreeningFacade screeningFacade, FilmFacade filmFacade) {
        var sampleFilms = FilmTestUtils.addSampleFilms(filmFacade);
        var sampleRooms = addSampleScreeningRooms(screeningFacade);
        var screening1 = screeningFacade.add(
                new AddScreeningDto(
                        LocalDateTime
                                .of(currentYear, 5, 5, 18, 30),
                        13,
                        sampleFilms.get(0).id(),
                        sampleRooms.get(0).id()
                )
        );
        var screening2 = screeningFacade.add(
                new AddScreeningDto(
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
