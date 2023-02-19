package code.films.application;

import code.films.application.dto.*;
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

    private final ScreeningFactory screeningFactory;

    private final ScreeningSearcher screeningSearcher;

    private final RoomFactory roomFactory;

    private final RoomSearcher roomSearcher;

    public FilmDto createFilm(CreateFilmDto dto) {
        return filmFactory.createFilm(dto).toDto();
    }

    public List<FilmDto> searchFilmsBy(FilmSearchParams params) {
        return filmSearcher.searchFilmsBy(params);
    }

    @Transactional
    public ScreeningDto createScreening(CreateScreeningDto dto) {
        return screeningFactory
                .createScreening(dto)
                .toDto();
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
