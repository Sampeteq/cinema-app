package code.screenings;

import code.screenings.exception.FilmNotFoundException;
import code.screenings.exception.ScreeningDateException;
import code.screenings.exception.ScreeningRoomException;
import code.screenings.exception.ScreeningRoomNotFoundException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
class ScreeningFactory {

    private final ScreeningDateSpecification dateSpecification;

    private final FilmRepository filmRepository;

    private final ScreeningRepository screeningRepository;

    private final ScreeningRoomRepository screeningRoomRepository;

    Screening createScreening(LocalDateTime date, int minAge, UUID filmId, UUID roomId) {
        validateScreeningDate(date);
        validateScreeningRoomBeingBusy(date, roomId);
        var film = getFilmOrThrow(filmId);
        var room = getScreeningRoomOrThrow(roomId);
        var seats = createSeats(room.getSeatsInOneRowQuantity(), room.getRowsQuantity());
        var screening = Screening.of(
                date,
                minAge,
                film,
                room
        );
        screening.addSeats(seats);
        return screening;
    }

    private void validateScreeningDate(LocalDateTime date) {
        if (!dateSpecification.isSatisfyBy(date)) {
            if (dateSpecification instanceof CurrentOrNextOneYearScreeningDateSpecification) {
                throw new ScreeningDateException("A screening date year must be current or next one");
            } else {
                throw new IllegalArgumentException("Unsupported screening date specification");
            }
        }
    }

    private Film getFilmOrThrow(UUID filmId) {
        return filmRepository
                .findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
    }

    private void validateScreeningRoomBeingBusy(LocalDateTime date, UUID roomUuid) {
        if (screeningRepository.existsByDateAndRoomId(date, roomUuid)) {
            throw new ScreeningRoomException("Screening room busy: " + roomUuid);
        }
    }

    private ScreeningRoom getScreeningRoomOrThrow(UUID roomId) {
        return screeningRoomRepository
                .findById(roomId)
                .orElseThrow(() -> new ScreeningRoomNotFoundException(roomId));
    }

    private List<Seat> createSeats(int seatsInOneRowQuantity, int rowsQuantity) {
        var seats = new ArrayList<Seat>();
        var rowNumber = 1;
        var seatNumber = 1;
        var helpCounter = 1;
        for (int i = 1; i <= seatsInOneRowQuantity * rowsQuantity; i++) {
            if (helpCounter > seatsInOneRowQuantity) {
                rowNumber++;
                seatNumber = 1;
                helpCounter = 1;
            }
            var seat = new Seat(
                    UUID.randomUUID(),
                    rowNumber,
                    seatNumber++,
                    SeatStatus.FREE,
                    null
            );
            seats.add(seat);
            helpCounter++;
        }
        return seats;
    }
}
