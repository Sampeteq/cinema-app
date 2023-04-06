package code.utils;

import code.films.applications.dto.CreateFilmDto;
import code.films.applications.dto.CreateScreeningDto;
import code.films.applications.dto.FilmDto;
import code.rooms.infrastructure.rest.RoomDto;
import code.films.applications.dto.ScreeningDto;
import code.bookings.application.dto.SeatDto;
import code.films.applications.services.mappers.FilmMapper;
import code.films.applications.services.mappers.ScreeningMapper;
import code.films.domain.Film;
import code.films.domain.FilmCategory;
import code.films.domain.FilmRepository;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.films.domain.Screening;
import code.films.domain.ScreeningRepository;
import code.bookings.domain.Seat;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class FilmTestHelper {

    private final FilmRepository filmRepository;

    private final FilmMapper filmMapper;

    private final ScreeningRepository screeningRepository;

    private final ScreeningMapper screeningMapper;

    private final RoomRepository roomRepository;

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

    public FilmDto addSampleFilm() {
        var film = createSampleFilm();
        filmRepository.save(film);
        return filmMapper.mapToDto(film);
    }

    public FilmDto addSampleFilmWithoutScreenings() {
        var film = new Film(
                UUID.randomUUID(),
                "Sample title",
                FilmCategory.COMEDY,
                currentYear,
                120,
                new ArrayList<>()
        );
        filmRepository.save(film);
        return filmMapper.mapToDto(film);
    }

    public FilmDto addSampleFilm(FilmCategory category) {
        var film = createSampleFilm().withCategory(category);
        filmRepository.save(film);
        return filmMapper.mapToDto(film);
    }


    public List<FilmDto> addSampleFilms() {
        var film1 = new Film(
                UUID.randomUUID(),
                "title 1",
                FilmCategory.COMEDY,
                Year.now().getValue(),
                120,
                new ArrayList<>()
        );
        var film2 = new Film(
                UUID.randomUUID(),
                "title 2",
                FilmCategory.DRAMA,
                Year.now().getValue() - 1,
                90,
                new ArrayList<>()
        );
        var sampleFilm1 = filmRepository.save(film1);
        var sampleFilm2 = filmRepository.save(film2);
        return Stream.of(sampleFilm1, sampleFilm2)
                .map(filmMapper::mapToDto)
                .toList();
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

    public ScreeningDto addSampleScreening() {
        var sampleFilm = createSampleFilm();
        filmRepository.save(sampleFilm);
        var screening = sampleFilm.getScreenings().get(0);
        return screeningMapper.mapToDto(screening);
    }

    public ScreeningDto addSampleScreening(LocalDateTime screeningDate) {
        var sampleFilm = createSampleFilm();
        filmRepository.save(sampleFilm);

        var sampleRoom = createSampleRoom();
        roomRepository.save(sampleRoom);

        var screening = createSampleScreening(sampleFilm, sampleRoom).withDate(screeningDate);
        screeningRepository.save(screening);
        return screeningMapper.mapToDto(screening);
    }

    public List<ScreeningDto> sampleScreenings() {
        var sampleFilm = createSampleFilm();
        filmRepository.save(sampleFilm);

        var sampleRoom = createSampleRoom();
        roomRepository.save(sampleRoom);

        var screening1 = sampleFilm.getScreenings().get(0);
        var screening2 = sampleFilm.getScreenings().get(1);
        return Stream.of(screening1, screening2)
                .map(screeningMapper::mapToDto)
                .toList();
    }

    public RoomDto addSampleRoom() {
        var room = createSampleRoom();
        roomRepository.save(room);
        return room.toDto();
    }

    public List<SeatDto> searchScreeningSeats(UUID screeningId) {
        return screeningRepository
                .findById(screeningId)
                .get()
                .getSeats()
                .stream()
                .map(Seat::toDto)
                .toList();
    }

    private Film createSampleFilm() {
        var film = new Film(
                UUID.randomUUID(),
                "Sample title",
                FilmCategory.COMEDY,
                currentYear,
                120,
                new ArrayList<>()
        );
        var room = createSampleRoom();
        var screening1 = Screening.of(
                        LocalDateTime
                                .of(currentYear, 5, 5, 18, 30),
                        13,
                        film,
                        room
        );
        var screening2 =
                Screening.of(
                        LocalDateTime.of(currentYear, 7, 3, 20, 30),
                        18,
                        film,
                        room
                );
        film.addScreening(screening1);
        film.addScreening(screening2);
        return film;
    }

    private Room createSampleRoom() {
        return new Room(
                UUID.randomUUID(),
                1,
                10,
                15
        );
    }

    private Screening createSampleScreening(Film film, Room room) {
        return Screening.of(
                LocalDateTime.of(currentYear, 5, 10, 18, 30),
                13,
                film,
                room
        );
    }
}
