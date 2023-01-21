package code.films;

import code.films.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class FilmFacade {

    private final FilmFactory filmFactory;

    private final FilmSearcher filmSearcher;

    private final ScreeningRoomFactory screeningRoomFactory;

    private final ScreeningRoomSearcher screeningRoomSearcher;

    private final ScreeningFactory screeningFactory;

    private final ScreeningSearcher screeningSearcher;

    private final SeatBooker seatBooker;

    private final SeatBookingSearcher seatBookingSearcher;

    public FilmDto createFilm(CreateFilmDto dto) {
        return filmFactory.createFilm(dto).toDto();
    }

    public List<FilmDto> searchFilms(FilmSearchParamsDto paramsDto) {
        return filmSearcher.searchFilms(paramsDto);
    }

    @Transactional
    public ScreeningRoomDto createScreeningsRoom(CreateScreeningRoomDto dto) {
        return screeningRoomFactory.createRoom(dto);
    }

    public List<ScreeningRoomDto> searchScreeningsRooms() {
        return screeningRoomSearcher.searchAllRooms();
    }

    @Transactional
    public ScreeningDto createScreening(CreateScreeningDto dto) {
        return screeningFactory
                .createScreening(dto)
                .toDto();
    }

    @Transactional
    public List<ScreeningDto> searchScreeningsBy(ScreeningSearchParamsDto paramsDto) {
        return screeningSearcher.searchBy(paramsDto);
    }

    @Transactional
    public List<SeatDto> searchScreeningSeats(UUID screeningId) {
        return screeningSearcher.searchSeatsByScreeningId(screeningId);
    }

    @Transactional
    public SeatBookingDto bookSeat(BookSeatDto dto, String username, Clock clock) {
        return seatBooker.book(dto, username, clock);
    }

    @Transactional
    public void cancelSeatBooking(UUID bookingId, Clock clock) {
        seatBooker.cancel(bookingId, clock);
    }

    public SeatBookingDto searchSeatBooking(UUID bookingId, String username) {
        return seatBookingSearcher.searchSeatBooking(bookingId, username);
    }

    public List<SeatBookingDto> searchSeatBookingsByUsername(String username) {
        return seatBookingSearcher.searchAllByUsername(username);
    }
}
