package code.screenings;

import code.films.FilmFacade;
import code.films.exception.FilmNotFoundException;
import code.screenings.dto.*;
import code.screenings.exception.ScreeningNotFoundException;
import code.screenings.exception.ScreeningRoomAlreadyExistsException;
import code.screenings.exception.ScreeningRoomBusyException;
import code.screenings.exception.ScreeningRoomNotFoundException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ScreeningFacade {

    private final ScreeningRepository screeningRepository;

    private final ScreeningRoomRepository screeningRoomRepository;

    private final FilmFacade filmFacade;

    public ScreeningDTO add(AddScreeningDTO dto, Year currentYear) {
        validateFilmExisting(dto.filmId());
        var room = getScreeningRoomOrThrow(dto.roomUuid());
        var date = ScreeningDate.of(dto.date(), currentYear);
        validateScreeningRoomBeingBusy(date, dto.roomUuid());
        var screening = new Screening(
                ScreeningDate.of(dto.date(), currentYear),
                dto.minAge(),
                dto.freeSeatsQuantity(),
                dto.filmId(),
                room
        );
        return screeningRepository
                .save(screening)
                .toDTO();
    }

    public ScreeningDTO read(Long screeningId) {
        return getScreeningOrThrow(screeningId).toDTO();
    }

    public List<ScreeningDTO> readAll() {
        return screeningRepository.findAllAsDTO();
    }

    public List<ScreeningDTO> readByFilmId(Long filmId) {
        return screeningRepository
                .findByFilmId(filmId)
                .stream()
                .map(Screening::toDTO)
                .toList();
    }

    public List<ScreeningDTO> readByDate(LocalDateTime date, Year currentYear) {
        var screeningDate = ScreeningDate.of(date, currentYear);
        return screeningRepository
                .findByDate(screeningDate)
                .stream()
                .map(Screening::toDTO)
                .toList();
    }

    public ScreeningRoomDTO addRoom(AddScreeningRoomDTO dto) {
        if (screeningRoomRepository.existsByNumber(dto.number())) {
            throw new ScreeningRoomAlreadyExistsException(dto.number());
        }
        var screeningRoom = new ScreeningRoom(dto.number(), dto.freeSeats());
        return screeningRoomRepository
                .save(screeningRoom)
                .toDTO();
    }

    public ScreeningRoomDTO readRoom(UUID roomUuid) {
        return screeningRoomRepository
                .findById(roomUuid)
                .map(ScreeningRoom::toDTO)
                .orElseThrow(() -> new ScreeningRoomNotFoundException(roomUuid));
    }

    public List<ScreeningRoomDTO> readAllRooms() {
        return screeningRoomRepository
                .findAll()
                .stream()
                .map(ScreeningRoom::toDTO)
                .toList();
    }

    public ScreeningReservationData fetchReservationData(Long screeningId) {
        return screeningRepository
                .findByIdAsReservationData(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));
    }

    public LocalDateTime fetchScreeningDate(Long screeningId) {
        return getScreeningOrThrow(screeningId)
                .toDTO()
                .date();
    }

    private Screening getScreeningOrThrow(Long screeningId) {
        return screeningRepository
                .findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));
    }

    private void validateFilmExisting(Long filmId) {
        if (!filmFacade.isPresent(filmId)) {
            throw new FilmNotFoundException(filmId);
        }
    }

    private ScreeningRoom getScreeningRoomOrThrow(UUID roomUuid) {
        return screeningRoomRepository
                .findById(roomUuid)
                .orElseThrow(() -> new ScreeningRoomNotFoundException(roomUuid));
    }

    private void validateScreeningRoomBeingBusy(ScreeningDate date, UUID roomUuid) {
        if (screeningRepository.existsByDateAndRoom_uuid(date, roomUuid)) {
            throw new ScreeningRoomBusyException(roomUuid);
        }
    }
}
