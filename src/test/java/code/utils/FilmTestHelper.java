package code.utils;

import code.films.application.FilmFacade;
import code.films.application.dto.*;
import code.films.domain.FilmCategory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Component
public class FilmTestHelper {

    private final FilmFacade filmFacade;

    private static final int currentYear = Year.now().getValue();

    public static CreateFilmDto createCreateFilmDto() {
        return new CreateFilmDto(
                "title 1",
                FilmCategory.COMEDY,
                Year.now().getValue(),
                120
        );
    }

    public static List<CreateFilmDto> createCreateFilmDtos() {
        var dto1 = new CreateFilmDto(
                "title 1",
                FilmCategory.COMEDY,
                Year.now().getValue(),
                120
        );
        var dto2 = new CreateFilmDto(
                "title 2",
                FilmCategory.DRAMA,
                Year.now().getValue() - 1,
                90
        );
        return List.of(dto1, dto2);
    }

    public static List<Integer> getWrongFilmYears() {
        var currentYear = Year.now();
        return List.of(
                currentYear.minusYears(2).getValue(),
                currentYear.plusYears(2).getValue()
        );
    }

    public FilmDto createFilm() {
        var filmCreatingRequest = createCreateFilmDto();
        return filmFacade.createFilm(filmCreatingRequest);
    }

    public FilmDto createFilm(FilmCategory category) {
        var filmCreatingRequest = createCreateFilmDto().withFilmCategory(category);
        return filmFacade.createFilm(filmCreatingRequest);
    }

    public List<FilmDto> createFilms() {
        return createCreateFilmDtos()
                .stream()
                .map(filmFacade::createFilm)
                .toList();
    }

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
        var sampleFilm = createFilm();
        var sampleRoom = createScreeningRoom();
        return filmFacade.createScreening(
                createCreateScreeningDto(
                        sampleFilm.id(),
                        sampleRoom.id()
                )
        );
    }

    public ScreeningDto createScreening(LocalDateTime screeningDate) {
        var sampleFilm = createFilm();
        var sampleRoom = createScreeningRoom();
        return filmFacade.createScreening(
                createCreateScreeningDto(
                        sampleFilm.id(),
                        sampleRoom.id()
                ).withDate(screeningDate)
        );
    }

    public List<ScreeningDto> createScreenings() {
        var sampleFilms = createFilms();
        var sampleRooms = createScreeningRooms();
        var screening1 = filmFacade.createScreening(
                new CreateScreeningDto(
                        LocalDateTime
                                .of(currentYear, 5, 5, 18, 30),
                        13,
                        sampleFilms.get(0).id(),
                        sampleRooms.get(0).id()
                )
        );
        var screening2 = filmFacade.createScreening(
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
        return filmFacade.createRoom(
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
                filmFacade.createRoom(room1),
                filmFacade.createRoom(room2)
        );
    }


    public List<SeatDto> searchScreeningSeats(UUID screeningId) {
        return filmFacade.searchSeats(screeningId);
    }
}
