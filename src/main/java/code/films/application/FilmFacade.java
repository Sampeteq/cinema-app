package code.films.application;

import code.films.application.dto.*;
import code.films.application.internal.FilmMapper;
import code.films.application.internal.FilmSearcher;
import code.films.application.internal.RoomSearcher;
import code.films.application.internal.ScreeningMapper;
import code.films.application.internal.ScreeningSearcher;
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

    private final FilmSearcher filmSearcher;

    private final FilmMapper filmMapper;

    private final ScreeningFactory screeningFactory;

    private final ScreeningSearcher screeningSearcher;

    private final ScreeningMapper screeningMapper;

    private final RoomFactory roomFactory;

    private final RoomSearcher roomSearcher;

    public FilmDto createFilm(CreateFilmDto dto) {
        var film = filmFactory.createFilm(dto);
        return filmMapper.mapToDto(film);
    }

    public List<FilmDto> searchFilmsBy(FilmSearchParams params) {
        return filmSearcher.searchFilmsBy(params);
    }

    @Transactional
    public ScreeningDto createScreening(CreateScreeningDto dto) {
        var screening = screeningFactory.createScreening(dto);
        return screeningMapper.mapToDto(screening);
    }

    @Transactional
    public List<ScreeningDto> searchScreeningsBy(ScreeningSearchParams params) {
        return screeningSearcher.searchScreeningsBy(params);
    }

    @Transactional
    public List<SeatDto> searchSeats(UUID screeningId) {
        return screeningSearcher.searchSeats(screeningId);
    }

    public SeatDetails searchSeatDetails(UUID seatId, Clock clock) {
        return screeningSearcher.searchScreeningDetails(seatId, clock);
    }

    @Transactional
    public RoomDto createRoom(CreateRoomDto dto) {
        return roomFactory.createRoom(dto);
    }

    public List<RoomDto> searchAllRooms() {
        return roomSearcher.searchAllRooms();
    }
}
