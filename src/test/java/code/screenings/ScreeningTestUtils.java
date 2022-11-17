package code.screenings;

import code.films.FilmFacade;
import code.films.FilmTestUtils;
import code.screenings.dto.AddScreeningDTO;
import code.screenings.dto.AddScreeningRoomDTO;
import code.screenings.dto.ScreeningDTO;
import code.screenings.dto.ScreeningRoomDTO;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

public class ScreeningTestUtils {

    public static final Year currentYear = Year.now();

    public static AddScreeningDTO sampleAddScreeningDTO(UUID filmId, UUID roomId) {
        return AddScreeningDTO
                .builder()
                .filmId(filmId)
                .roomId(roomId)
                .date(LocalDateTime.parse("2022-05-05T16:30"))
                .minAge(13)
                .build();
    }

    public static AddScreeningDTO sampleAddScreeningDTO(UUID filmId, UUID roomId, int wrongScreeningYear) {
        return AddScreeningDTO
                .builder()
                .filmId(filmId)
                .roomId(roomId)
                .date(LocalDateTime.parse("2022-05-05T16:30").withYear(wrongScreeningYear))
                .minAge(13)
                .build();
    }


    public static AddScreeningRoomDTO sampleAddRoomDTO() {
        return AddScreeningRoomDTO
                .builder()
                .number(1)
                .rowsQuantity(9)
                .seatsQuantityInOneRow(6)
                .build();
    }

    public static List<ScreeningRoomDTO> addSampleScreeningRooms(ScreeningFacade screeningFacade) {
        var room1 = AddScreeningRoomDTO
                .builder()
                .number(1)
                .rowsQuantity(9)
                .seatsQuantityInOneRow(6)
                .build();
        var room2 = AddScreeningRoomDTO
                .builder()
                .number(2)
                .rowsQuantity(7)
                .seatsQuantityInOneRow(5)
                .build();
        return List.of(
                screeningFacade.addRoom(room1),
                screeningFacade.addRoom(room2)
        );
    }

    public static List<ScreeningDTO> addSampleScreenings(ScreeningFacade screeningFacade, FilmFacade filmFacade) {
        var sampleFilms = FilmTestUtils.addSampleFilms(filmFacade);
        var sampleRooms = addSampleScreeningRooms(screeningFacade);
        var screening1 = screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .filmId(sampleFilms.get(0).id())
                        .roomId(sampleRooms.get(0).id())
                        .date(LocalDateTime.parse("2022-05-05T16:00"))
                        .minAge(13)
                        .build()
        );
        var screening2 = screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .filmId(sampleFilms.get(1).id())
                        .roomId(sampleRooms.get(1).id())
                        .date(LocalDateTime.parse("2022-06-09T17:30"))
                        .minAge(13)
                        .build()
        );
        return List.of(screening1, screening2);
    }

    public static List<LocalDateTime> wrongScreeningDates() {
        var date1 = LocalDateTime.of(
                currentYear.getValue() - 1,
                2,
                2,
                16,
                30
        );
        var date2= LocalDateTime.of(
                currentYear.getValue() + 2,
                2,
                2,
                16,
                30
        );
        return List.of(date1, date2);
    }
}
