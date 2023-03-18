package code.films.application;

import code.films.application.dto.*;
import code.films.application.internal.mappers.FilmMapper;
import code.films.application.internal.FilmSearchService;
import code.films.application.internal.RoomSearchService;
import code.films.application.internal.mappers.ScreeningMapper;
import code.films.application.internal.ScreeningSearchService;
import code.films.domain.FilmFactory;
import code.films.domain.RoomFactory;
import code.films.domain.ScreeningFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class FilmFacade {

    private final FilmFactory filmFactory;

    private final FilmSearchService filmSearchService;

    private final FilmMapper filmMapper;

    private final ScreeningFactory screeningFactory;

    private final ScreeningSearchService screeningSearchService;

    private final ScreeningMapper screeningMapper;

    private final RoomFactory roomFactory;

    private final RoomSearchService roomSearchService;

    private final Clock clock;

    public FilmDto createFilm(CreateFilmDto dto) {
        var film = filmFactory.createFilm(dto);
        return filmMapper.mapToDto(film);
    }

    public List<FilmDto> searchFilmsBy(FilmSearchParams params) {
        return filmSearchService.searchFilmsBy(params);
    }

    @Transactional
    public ScreeningDto createScreening(CreateScreeningDto dto) {
        var screening = screeningFactory.createScreening(dto);
        return screeningMapper.mapToDto(screening);
    }

    @Transactional
    public List<ScreeningDto> searchScreeningsBy(ScreeningSearchParams params) {
        return screeningSearchService.searchScreeningsBy(params);
    }

    @Transactional
    public List<SeatDto> searchSeats(UUID screeningId) {
        return screeningSearchService.searchSeats(screeningId);
    }

    public SeatDetails searchSeatDetails(UUID seatId) {
        return screeningSearchService.searchScreeningDetails(seatId, clock);
    }

    @Transactional
    public RoomDto createRoom(CreateRoomDto dto) {
        return roomFactory.createRoom(dto);
    }

    public List<RoomDto> searchAllRooms() {
        return roomSearchService.searchAllRooms();
    }
}
