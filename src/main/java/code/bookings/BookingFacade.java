package code.bookings;

import code.bookings.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class BookingFacade {

    private final FilmFactory filmFactory;

    private final FilmSearcher filmSearcher;

    private final RoomFactory roomFactory;

    private final RoomSearcher roomSearcher;

    private final ScreeningFactory screeningFactory;

    private final ScreeningSearcher screeningSearcher;

    private final Booker booker;

    private final BookingSearcher bookingSearcher;

    public FilmDto createFilm(CreateFilmDto dto) {
        return filmFactory.createFilm(dto).toDto();
    }

    public List<FilmDto> searchFilms(FilmSearchParamsDto paramsDto) {
        return filmSearcher.searchFilms(paramsDto);
    }

    @Transactional
    public RoomDto createScreeningsRoom(CreateRoomDto dto) {
        return roomFactory.createRoom(dto);
    }

    public List<RoomDto> searchScreeningsRooms() {
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
        return screeningSearcher.searchBy(paramsDto);
    }

    @Transactional
    public List<SeatDto> searchScreeningSeats(UUID screeningId) {
        return screeningSearcher.searchSeatsByScreeningId(screeningId);
    }

    @Transactional
    public BookingDto bookSeat(BookDto dto, String username, Clock clock) {
        return booker.book(dto, username, clock);
    }

    @Transactional
    public void cancelSeatBooking(UUID bookingId, Clock clock) {
        booker.cancel(bookingId, clock);
    }

    public BookingDto searchSeatBooking(UUID bookingId, String username) {
        return bookingSearcher.searchSeatBooking(bookingId, username);
    }

    public List<BookingDto> searchSeatBookingsByUsername(String username) {
        return bookingSearcher.searchAllByUsername(username);
    }
}
