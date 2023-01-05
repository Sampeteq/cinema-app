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

    private final ScreeningRoomRepository screeningRoomRepository;

    private final SeatBookingRepository seatBookingRepository;

    private final ScreeningSearcher screeningSearcher;

    private final ScreeningRoomFactory screeningRoomFactory;

    private final ScreeningFactory screeningFactory;

    private final SeatBooker seatBooker;

    private final ScreeningRepository screeningRepository;

    @Transactional
    public ScreeningDto createScreening(CreateScreeningDto dto) {
        var screening = screeningFactory.createScreening(
                dto.date(),
                dto.minAge(),
                dto.filmId(),
                dto.roomId()
        );
        return screeningRepository
                .add(screening)
                .toDTO();
    }

    @Transactional
    public List<ScreeningDto> searchScreeningsBy(ScreeningSearchParamsDto paramsDto) {
        return screeningSearcher.searchBy(paramsDto);
    }

    @Transactional
    public ScreeningRoomDto createRoom(CreateScreeningRoomDto dto) {
        return screeningRoomFactory.createRoom(dto);
    }

    public List<ScreeningRoomDto> searchAllRooms() {
        return screeningRoomRepository
                .getAll()
                .stream()
                .map(ScreeningRoom::toDTO)
                .toList();
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
                .getByIdAndUsername(ticketId, username)
                .orElseThrow(() -> new SeatBookingNotFoundException(ticketId));
    }
}
