package code.utils;

import code.rooms.application.RoomFacade;
import code.rooms.domain.dto.CreateRoomDto;
import code.rooms.domain.dto.RoomDto;
import code.screenings.application.ScreeningFacade;
import code.screenings.domain.dto.CreateScreeningDto;
import code.screenings.domain.dto.ScreeningDto;
import code.screenings.domain.dto.SeatDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@Component
public class ScreeningTestHelper {

    private final ScreeningFacade screeningFacade;

    private final RoomFacade roomFacade;

    private final FilmTestHelper filmTestHelper;

    private static final int currentYear = Year.now().getValue();

    public static CreateRoomDto createScreeningRoomDto() {
        return new CreateRoomDto(
                1,
                6,
                10
        );
    }

    public static CreateScreeningDto createCreateScreeningDto(UUID filmId, UUID roomId) {
        return new CreateScreeningDto(
                LocalDateTime.of(currentYear, 5, 10, 18, 30),
                13,
                filmId,
                roomId
        );
    }

    public static CreateScreeningDto createCreateScreeningDto(
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

    public ScreeningDto createScreening() {
        var sampleFilm = filmTestHelper.createFilm();
        var sampleRoom = createScreeningRoom();
        return screeningFacade.createScreening(
                createCreateScreeningDto(
                        sampleFilm.id(),
                        sampleRoom.id()
                )
        );
    }

    public ScreeningDto createScreening(LocalDateTime screeningDate) {
        var sampleFilm = filmTestHelper.createFilm();
        var sampleRoom = createScreeningRoom();
        return screeningFacade.createScreening(
                createCreateScreeningDto(
                        sampleFilm.id(),
                        sampleRoom.id()
                ).withDate(screeningDate)
        );
    }

    public List<ScreeningDto> createScreenings() {
        var sampleFilms = filmTestHelper.createFilms();
        var sampleRooms = createScreeningRooms();
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

    public RoomDto createScreeningRoom() {
        return roomFacade.createRoom(
                createScreeningRoomDto()
        );
    }

    public List<RoomDto> createScreeningRooms() {
        var room1 = new CreateRoomDto(
                1,
                6,
                10
        );
        var room2 = new CreateRoomDto(
                2,
                8,
                7
        );
        return List.of(
                roomFacade.createRoom(room1),
                roomFacade.createRoom(room2)
        );
    }


    public List<SeatDto> searchScreeningSeats(UUID screeningId) {
        return screeningFacade.searchSeats(screeningId);
    }
}
