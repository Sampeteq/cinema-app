package code.screenings;

import code.bookings.dto.BookingCancelledEvent;
import code.bookings.dto.SeatBookedEvent;
import code.screenings.dto.*;
import code.screenings.exception.ScreeningNotFoundException;
import code.screenings.exception.SeatNotFoundException;
import com.google.common.eventbus.Subscribe;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ScreeningFacade {

    private final FilmFactory filmFactory;

    private final FilmSearcher filmSearcher;

    private final RoomFactory roomFactory;

    private final RoomSearcher roomSearcher;

    private final ScreeningFactory screeningFactory;

    private final ScreeningSearcher screeningSearcher;

    private final ScreeningRepository screeningRepository;

    public FilmDto createFilm(CreateFilmDto dto) {
        return filmFactory.createFilm(dto).toDto();
    }

    public List<FilmDto> searchFilmsBy(FilmSearchParamsDto paramsDto) {
        return filmSearcher.searchFilmsBy(paramsDto);
    }

    @Transactional
    public RoomDto createRoom(CreateRoomDto dto) {
        return roomFactory.createRoom(dto);
    }

    public List<RoomDto> searchAllRooms() {
        return roomSearcher.searchAllRooms();
    }

    @Transactional
    public ScreeningDto createScreening(CreateScreeningDto dto) {
        return screeningFactory
                .createScreening(dto)
                .toDto();
    }

    @Transactional
    public List<ScreeningDto> searchScreeningsBy(ScreeningSearchParamsDto paramsDto) {
        return screeningSearcher.searchScreeningsBy(paramsDto);
    }

    @Transactional
    public List<SeatDto> searchSeats(UUID screeningId) {
        return screeningSearcher.searchSeats(screeningId);
    }

    public ScreeningDetails getScreeningDetails(UUID screeningId, UUID seatId, Clock clock) {
        var screening = screeningRepository
                .findById(screeningId)
                .orElseThrow(ScreeningNotFoundException::new);
        var timeToScreeningStartInHours = screening.timeToScreeningStartInHours(clock);
        var isFree = screening
                .getSeat(seatId)
                .orElseThrow(SeatNotFoundException::new)
                .isFree();
        return new ScreeningDetails(
                timeToScreeningStartInHours,
                isFree
        );
    }

    @Subscribe
    @Transactional
    public void handle(SeatBookedEvent event) {
        screeningRepository
                .findById(event.screeningId())
                .orElseThrow(ScreeningNotFoundException::new)
                .getSeat(event.seatId())
                .orElseThrow(SeatNotFoundException::new)
                .changeStatus(SeatStatus.BUSY);
    }

    @Subscribe
    @Transactional
    public void handle(BookingCancelledEvent event) {
        screeningRepository
                .findById(event.screeningId())
                .orElseThrow(ScreeningNotFoundException::new)
                .getSeat(event.seatId())
                .orElseThrow(SeatNotFoundException::new)
                .changeStatus(SeatStatus.FREE);
    }
}
