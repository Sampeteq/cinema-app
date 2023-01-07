package code.screenings;

import code.screenings.dto.*;
import code.screenings.exception.SeatBookingNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ScreeningFacade {

    private final FilmFactory filmFactory;

    private final FilmRepository filmRepository;

    private final ScreeningRoomFactory screeningRoomFactory;

    private final ScreeningRoomRepository screeningRoomRepository;

    private final ScreeningFactory screeningFactory;

    private final ScreeningRepository screeningRepository;

    private final SeatBookingRepository seatBookingRepository;

    private final ScreeningSearcher screeningSearcher;

    private final SeatBooker seatBooker;

    public FilmDto createFilm(CreateFilmDto dto) {
        var film = filmFactory.createFilm(
                dto.title(),
                FilmCategory.fromDTO(dto.filmCategory()),
                dto.year(),
                dto.durationInMinutes()
        );
        return filmRepository
                .save(film)
                .toDTO();
    }

    public List<FilmDto> searchFilms(FilmSearchParamsDto paramsDto) {
        var params = FilmSearchParams
                .builder()
                .category(paramsDto.category == null ? null : FilmCategory.fromDTO(paramsDto.category))
                .build();
        return filmRepository
                .findBy(params)
                .stream()
                .map(Film::toDTO)
                .toList();
    }

    @Transactional
    public ScreeningRoomDto createRoom(CreateScreeningRoomDto dto) {
        return screeningRoomFactory.createRoom(dto);
    }

    public List<ScreeningRoomDto> searchAllRooms() {
        return screeningRoomRepository
                .findAll()
                .stream()
                .map(ScreeningRoom::toDTO)
                .toList();
    }

    @Transactional
    public ScreeningDto createScreening(CreateScreeningDto dto) {
        var screening = screeningFactory.createScreening(
                dto.date(),
                dto.minAge(),
                dto.filmId(),
                dto.roomId()
        );
        return screeningRepository
                .save(screening)
                .toDTO();
    }

    @Transactional
    public List<ScreeningDto> searchScreeningsBy(ScreeningSearchParamsDto paramsDto) {
        return screeningSearcher.searchBy(paramsDto);
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
        return getBookingOrThrow(bookingId, username).toDTO();
    }

    private SeatBooking getBookingOrThrow(UUID ticketId, String username) {
        return seatBookingRepository
                .findByIdAndUsername(ticketId, username)
                .orElseThrow(() -> new SeatBookingNotFoundException(ticketId));
    }
}
