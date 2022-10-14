package code.screening;

import code.screening.dto.AddScreeningDTO;
import code.screening.dto.AddScreeningRoomDTO;
import code.screening.dto.ScreeningDTO;
import code.screening.dto.ScreeningRoomDTO;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

public class ScreeningTestUtils {

    public static final Year currentYear = Year.now();

    public static AddScreeningDTO sampleAddScreeningDTO(Long filmId, UUID roomId) {
        return AddScreeningDTO
                .builder()
                .filmId(filmId)
                .roomUuid(roomId)
                .date(LocalDateTime.parse("2022-05-05T16:30"))
                .minAge(13)
                .build();
    }

    public static ScreeningDTO addSampleScreening(Long filmId, UUID roomUuid,  ScreeningFacade screeningFacade) {
        var dto = AddScreeningDTO
                .builder()
                .filmId(filmId)
                .roomUuid(roomUuid)
                .date(LocalDateTime.parse("2022-05-05T16:30"))
                .minAge(13)
                .build();
        return screeningFacade.add(dto, currentYear);
    }

    public static ScreeningDTO addSampleScreeningWithWrongFilmYear(Long filmId, ScreeningFacade screeningFacade) {
        var dto = AddScreeningDTO
                .builder()
                .filmId(filmId)
                .date(LocalDateTime.of(currentYear.getValue() - 1, 1, 1, 18, 30))
                .minAge(13)
                .build();
        return screeningFacade.add(dto, currentYear);
    }

    public static ScreeningDTO addSampleScreeningWithNoFreeSeats(Long sampleFilmId, UUID sampleRoomUuid, ScreeningFacade screeningFacade) {
        return screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .filmId(sampleFilmId)
                        .roomUuid(sampleRoomUuid)
                        .date(LocalDateTime.parse("2022-05-05T16:30"))
                        .minAge(13)
                        .build(),
                currentYear
        );
    }

    public static List<ScreeningDTO> addSampleDistinctScreenings(Long filmId, UUID roomUuid, ScreeningFacade screeningFacade) {
        var screening1 = screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .filmId(filmId)
                        .roomUuid(roomUuid)
                        .date(LocalDateTime.parse("2022-05-05T16:00"))
                        .minAge(13)
                        .build(),
                currentYear
        );
        var screening2 = screeningFacade.add(
                AddScreeningDTO
                        .builder()
                        .filmId(filmId)
                        .roomUuid(roomUuid)
                        .date(LocalDateTime.parse("2022-06-09T17:30"))
                        .minAge(13)
                        .build(),
                currentYear
        );
        return List.of(screening1, screening2);
    }

    public static List<Integer> getWrongScreeningYears() {
        var currentYear= Year.now().getValue();
        return List.of(currentYear - 1, currentYear + 2);
    }

    public static ScreeningRoomDTO addSampleScreeningRoom(ScreeningFacade screeningFacade) {
        var dto = AddScreeningRoomDTO
                .builder()
                .number(1)
                .freeSeats(200)
                .build();
        return screeningFacade.addRoom(dto);
    }

    public static ScreeningRoomDTO addSampleScreeningRoomWithNoFreeSeats(ScreeningFacade screeningFacade) {
        var dto = AddScreeningRoomDTO
                .builder()
                .number(1)
                .freeSeats(0)
                .build();
        return screeningFacade.addRoom(dto);
    }
}
