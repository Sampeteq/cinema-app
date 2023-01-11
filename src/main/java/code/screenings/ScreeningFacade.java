package code.screenings;

import code.screenings.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ScreeningFacade {

    private final FilmFactory filmFactory;

    private final FilmSearcher filmSearcher;

    private final ScreeningRoomFactory screeningRoomFactory;

    private final ScreeningRoomSearcher screeningRoomSearcher;

    private final ScreeningFactory screeningFactory;

    private final ScreeningSearcher screeningSearcher;

    private final SeatBooker seatBooker;

    private final SeatBookingSearcher seatBookingSearcher;

    public FilmView createFilm(FilmCreatingRequest dto) {
        return filmFactory.createFilm(dto).toView();
    }

    public List<FilmView> searchFilms(FilmSearchParamsView paramsDto) {
        return filmSearcher.searchFilms(paramsDto);
    }

    @Transactional
    public ScreeningRoomView createRoom(ScreeningRoomCreatingRequest dto) {
        return screeningRoomFactory.createRoom(dto);
    }

    public List<ScreeningRoomView> searchAllRooms() {
        return screeningRoomSearcher.searchAllRooms();
    }

    @Transactional
    public ScreeningView createScreening(ScreeningCreatingRequest dto) {
        return screeningFactory
                .createScreening(dto)
                .toView();
    }

    @Transactional
    public List<ScreeningView> searchScreeningsBy(ScreeningSearchParamsView paramsDto) {
        return screeningSearcher.searchBy(paramsDto);
    }

    @Transactional
    public List<SeatView> searchScreeningSeats(UUID screeningId) {
        return screeningSearcher.searchSeatsByScreeningId(screeningId);
    }

    @Transactional
    public SeatBookingView bookSeat(SeatBookingRequest dto, String username, Clock clock) {
        return seatBooker.book(dto, username, clock);
    }

    @Transactional
    public void cancelSeatBooking(UUID bookingId, Clock clock) {
        seatBooker.cancel(bookingId, clock);
    }

    public SeatBookingView searchSeatBooking(UUID bookingId, String username) {
        return seatBookingSearcher.searchSeatBooking(bookingId, username);
    }

    public List<SeatBookingView> searchSeatBookingsByUsername(String username) {
        return seatBookingSearcher.searchAllByUsername(username);
    }
}
