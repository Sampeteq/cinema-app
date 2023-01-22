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

    @Transactional
    public BookingDto bookSeat(BookDto dto, String username, Clock clock) {
        return booker.bookSeat(dto, username, clock);
    }

    @Transactional
    public void cancelBooking(UUID bookingId, Clock clock) {
        booker.cancelSeat(bookingId, clock);
    }

    public BookingDto searchBookingById(UUID bookingId, String username) {
        return bookingSearcher.searchBookingById(bookingId, username);
    }

    public List<BookingDto> searchAllBookings(String username) {
        return bookingSearcher.searchAllBookings(username);
    }
}
