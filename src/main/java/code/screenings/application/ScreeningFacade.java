package code.screenings.application;

import code.bookings.domain.dto.BookingCancelledEvent;
import code.bookings.domain.dto.SeatBookedEvent;
import code.screenings.application.dto.*;
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
