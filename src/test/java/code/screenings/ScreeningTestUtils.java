package code.screenings;

import code.films.FilmFacade;
import code.films.SampleFilms;
import code.screenings.dto.AddScreeningDTO;
import code.screenings.dto.AddScreeningRoomDTO;
import code.screenings.dto.ScreeningDTO;
import code.screenings.dto.ScreeningRoomDTO;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

public class ScreeningTestUtils implements SampleFilms {

    public static final Year currentYear = Year.now();

    public static AddScreeningDTO sampleAddScreeningDTO(Long filmId, UUID roomId) {
        return AddScreeningDTO
                .builder()
                .filmId(filmId)
                .roomUuid(roomId)
                .date(LocalDateTime.parse("2022-05-05T16:30"))
                .minAge(13)
                .freeSeatsQuantity(200)
                .build();
    }

    public static AddScreeningDTO sampleAddScreeningDTO(Long filmId, UUID roomId, int wrongScreeningYear) {
        return AddScreeningDTO
                .builder()
                .filmId(filmId)
                .roomUuid(roomId)
                .date(LocalDateTime.parse("2022-05-05T16:30").withYear(wrongScreeningYear))
                .minAge(13)
                .freeSeatsQuantity(200)
                .build();
    }



    public static AddScreeningRoomDTO sampleAddRoomDTO() {
        return AddScreeningRoomDTO
                .builder()
                .freeSeats(200)
                .number(1)
                .build();
    }

    public static List<ScreeningRoomDTO> addSampleScreeningRooms(ScreeningFacade screeningFacade) {
        var room1 = AddScreeningRoomDTO
                .builder()
                .number(1)
                .freeSeats(200)
                .build();
        var room2 = AddScreeningRoomDTO
                .builder()
                .number(2)
                .freeSeats(200)
                .build();
        return List.of(
                screeningFacade.addRoom(room1),
                screeningFacade.addRoom(room2)
        );
    }

    public static ScreeningDTO addSampleScreeningWithNoFreeSeats(Long sampleFilmId,
                                                                 ScreeningFacade screeningFacade) {
        var screeningRoom = screeningFacade.addRoom(
                AddScreeningRoomDTO
                        .builder()
                        .number(5)
                        .freeSeats(0)
                        .build()
        );

        return screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .filmId(sampleFilmId)
                        .roomUuid(screeningRoom.uuid())
                        .date(LocalDateTime.parse("2022-05-05T16:30"))
                        .minAge(13)
                        .freeSeatsQuantity(0)
                        .build()
        );
    }

    public static List<ScreeningDTO> addSampleScreenings(ScreeningFacade screeningFacade, FilmFacade filmFacade) {
        var sampleFilms = SampleFilms.addSampleFilms(filmFacade);
        var sampleRooms = addSampleScreeningRooms(screeningFacade);
        var screening1 = screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .filmId(sampleFilms.get(0).id())
                        .roomUuid(sampleRooms.get(0).uuid())
                        .date(LocalDateTime.parse("2022-05-05T16:00"))
                        .minAge(13)
                        .freeSeatsQuantity(200)
                        .build()
        );
        var screening2 = screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .filmId(sampleFilms.get(1).id())
                        .roomUuid(sampleRooms.get(1).uuid())
                        .date(LocalDateTime.parse("2022-06-09T17:30"))
                        .minAge(13)
                        .freeSeatsQuantity(200)
                        .build()
        );
        return List.of(screening1, screening2);
    }

    public static List<Integer> getWrongScreeningYears() {
        return List.of(currentYear.getValue() - 1, currentYear.getValue() + 2);
    }
}
