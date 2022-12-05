package code.screenings;

import code.films.FilmFacade;
import code.films.exception.FilmNotFoundException;
import code.screenings.dto.AddScreeningDto;
import code.screenings.dto.ScreeningDto;
import code.screenings.exception.ScreeningRoomBusyException;
import code.screenings.exception.ScreeningRoomNotFoundException;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;

@AllArgsConstructor
class ScreeningCreator {

    private final ScreeningRepository screeningRepository;

    private final ScreeningRoomRepository screeningRoomRepository;

    private final FilmFacade filmFacade;

    public ScreeningDto add(AddScreeningDto dto) {
        var date = ScreeningDate.of(dto.date());
        var room = getScreeningRoomOrThrow(dto.roomId());
        validateScreeningRoomBeingBusy(date, dto.roomId());
        validateFilmExisting(dto.filmId());
        var seats = new ArrayList<ScreeningSeat>();
        var rowNumber = 1;
        var seatNumber = 1;
        var helpCounter = 1;
        for (int i = 1; i <= room.seatsQuantity(); i++) {
            if (helpCounter > room.getSeatsInOneRowQuantity()) {
                rowNumber++;
                seatNumber = 1;
                helpCounter = 1;
            }
            var seat = ScreeningSeat
                    .builder()
                    .id(UUID.randomUUID())
                    .rowNumber(rowNumber)
                    .number(seatNumber++)
                    .status(ScreeningSeatStatus.FREE)
                    .build();
            seats.add(seat);
            helpCounter++;
        }
        var screening = Screening
                .builder()
                .id(UUID.randomUUID())
                .date(date)
                .minAge(dto.minAge())
                .filmId(dto.filmId())
                .room(room)
                .seats(seats)
                .build();
        return screeningRepository
                .add(screening)
                .toDTO();
    }

    private void validateScreeningRoomBeingBusy(ScreeningDate date, UUID roomUuid) {
        if (screeningRepository.existsByDateAndRoomId(date, roomUuid)) {
            throw new ScreeningRoomBusyException(roomUuid);
        }
    }

    private ScreeningRoom getScreeningRoomOrThrow(UUID roomUuid) {
        return screeningRoomRepository
                .getById(roomUuid)
                .orElseThrow(() -> new ScreeningRoomNotFoundException(roomUuid));
    }

    private void validateFilmExisting(UUID filmId) {
        if (!filmFacade.isPresent(filmId)) {
            throw new FilmNotFoundException(filmId);
        }
    }
}
