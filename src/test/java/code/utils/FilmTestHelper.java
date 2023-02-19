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

    public static CreateFilmDto sampleCreateFilmDto() {
        return new CreateFilmDto(
                "title 1",
                FilmCategory.COMEDY,
                Year.now().getValue(),
                120
        );
    }

    public static List<Integer> sampleWrongFilmYears() {
        var currentYear = Year.now();
        return List.of(
                currentYear.minusYears(2).getValue(),
                currentYear.plusYears(2).getValue()
        );
    }

    public FilmDto sampleFilm() {
        var filmCreatingRequest = sampleCreateFilmDto();
        return filmFacade.createFilm(filmCreatingRequest);
    }

    public FilmDto sampleFilm(FilmCategory category) {
        var filmCreatingRequest = sampleCreateFilmDto().withFilmCategory(category);
        return filmFacade.createFilm(filmCreatingRequest);
    }

    public List<FilmDto> sampleFilms() {
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
        var sampleFilm1 = filmFacade.createFilm(dto1);
        var sampleFilm2 = filmFacade.createFilm(dto2);
        return List.of(sampleFilm1, sampleFilm2);
    }

    public static CreateRoomDto sampleScreeningRoomDto() {
        return new CreateRoomDto(
                1,
                6,
                10
        );
    }

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

    public static List<String> sampleWrongScreeningDates() {
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

    public ScreeningDto sampleScreening() {
        var sampleFilm = sampleFilm();
        var sampleRoom = sampleScreeningRoom();
        return filmFacade.createScreening(
                sampleCreateScreeningDto(
                        sampleFilm.id(),
                        sampleRoom.id()
                )
        );
    }

    public ScreeningDto sampleScreening(LocalDateTime screeningDate) {
        var sampleFilm = sampleFilm();
        var sampleRoom = sampleScreeningRoom();
        return filmFacade.createScreening(
                sampleCreateScreeningDto(
                        sampleFilm.id(),
                        sampleRoom.id()
                ).withDate(screeningDate)
        );
    }

    public List<ScreeningDto> sampleScreenings() {
        var sampleFilms = sampleFilms();
        var sampleRooms = sampleScreeningRooms();
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

    public RoomDto sampleScreeningRoom() {
        return filmFacade.createRoom(
                sampleScreeningRoomDto()
        );
    }

    public List<RoomDto> sampleScreeningRooms() {
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
