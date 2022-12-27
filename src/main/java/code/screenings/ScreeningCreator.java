package code.screenings;

import code.films.FilmFacade;
import code.films.exception.FilmNotFoundException;
import code.screenings.dto.AddScreeningDto;
import code.screenings.dto.ScreeningDto;
import code.screenings.exception.ScreeningRoomBusyException;
import code.screenings.exception.ScreeningRoomNotFoundException;
import lombok.AllArgsConstructor;

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
        var screening = new Screening(
                UUID.randomUUID(),
                date,
                dto.minAge(),
                dto.filmId(),
                room
        );
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
