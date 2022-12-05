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

public class ScreeningTestUtils {

    public static final Year currentYear = Year.now();

    public static AddScreeningDto sampleAddScreeningDTO(UUID filmId, UUID roomId) {
        return AddScreeningDto
                .builder()
                .filmId(filmId)
                .roomId(roomId)
                .date(LocalDateTime.parse("2022-05-05T16:30"))
                .minAge(13)
                .build();
    }

    public static AddScreeningDto sampleAddScreeningDTO(UUID filmId, UUID roomId, int wrongScreeningYear) {
        return AddScreeningDto
                .builder()
                .filmId(filmId)
                .roomId(roomId)
                .date(LocalDateTime.parse("2022-05-05T16:30").withYear(wrongScreeningYear))
                .minAge(13)
                .build();
    }


    public static AddScreeningRoomDto sampleAddRoomDTO() {
        return AddScreeningRoomDto
                .builder()
                .number(1)
                .rowsQuantity(9)
                .seatsQuantityInOneRow(6)
                .build();
    }

    public static List<ScreeningRoomDto> addSampleScreeningRooms(ScreeningFacade screeningFacade) {
        var room1 = AddScreeningRoomDto
                .builder()
                .number(1)
                .rowsQuantity(9)
                .seatsQuantityInOneRow(6)
                .build();
        var room2 = AddScreeningRoomDto
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

    public static List<ScreeningDto> addSampleScreenings(ScreeningFacade screeningFacade, FilmFacade filmFacade) {
        var sampleFilms = FilmTestUtils.addSampleFilms(filmFacade);
        var sampleRooms = addSampleScreeningRooms(screeningFacade);
        var screening1 = screeningFacade.add(
                AddScreeningDto
                        .builder()
                        .filmId(sampleFilms.get(0).id())
                        .roomId(sampleRooms.get(0).id())
                        .date(LocalDateTime.parse("2022-05-05T16:00"))
                        .minAge(13)
                        .build()
        );
        var screening2 = screeningFacade.add(
                AddScreeningDto
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
