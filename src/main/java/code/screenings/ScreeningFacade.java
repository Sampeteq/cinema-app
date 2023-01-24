package code.screenings;

import code.bookings.dto.BookingCancelledEvent;
import code.bookings.dto.SeatBookedEvent;
import code.screenings.dto.*;
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

    private final ScreeningFactory screeningFactory;

    private final ScreeningSearcher screeningSearcher;

    private final ScreeningEventHandler screeningEventHandler;

    public FilmDto createFilm(CreateFilmDto dto) {
        return filmFactory.createFilm(dto).toDto();
    }

    public List<FilmDto> searchFilmsBy(FilmSearchParamsDto paramsDto) {
        return filmSearcher.searchFilmsBy(paramsDto);
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

    public ScreeningDetails searchScreeningDetails(UUID screeningId, UUID seatId, Clock clock) {
        return screeningSearcher.searchScreeningDetails(screeningId, seatId, clock);
    }

    @Subscribe
    @Transactional
    public void handle(SeatBookedEvent event) {
        screeningEventHandler.handle(event);
    }

    @Subscribe
    @Transactional
    public void handle(BookingCancelledEvent event) {
        screeningEventHandler.handle(event);
    }
}
