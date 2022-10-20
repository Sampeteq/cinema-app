package code.screening;

import code.film.FilmFacade;
import code.film.exception.FilmNotFoundException;
import code.screening.dto.ScreeningReservationData;
import code.screening.dto.*;
import code.screening.exception.*;
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
        if (!filmFacade.isPresent(dto.filmId())) {
            throw new FilmNotFoundException(dto.filmId());
        }
        var room = screeningRoomRepository
                .findById(dto.roomUuid())
                .orElseThrow(() -> ScreeningRoomException.notFound(dto.roomUuid()));

        var screening = new Screening(
                ScreeningDate.of(dto.date(), currentYear),
                dto.minAge(),
                dto.filmId(),
                room
        );
        return screeningRepository
                .save(screening)
                .toDTO();
    }

    public ScreeningDTO read(Long screeningId) {
        return getScreeningOrThrowException(screeningId).toDTO();
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
        var screeningDate= ScreeningDate.of(date, currentYear);
        return screeningRepository
                .findByDate(screeningDate)
                .stream()
                .map(Screening::toDTO)
                .toList();
    }

    public ScreeningRoomDTO addRoom(AddScreeningRoomDTO dto) {
        if (screeningRoomRepository.existsByNumber(dto.number())) {
            throw ScreeningRoomException.alreadyExists(dto.number());
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
                .orElseThrow(() -> ScreeningRoomException.notFound(roomUuid));
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
                .orElseThrow(() -> ScreeningException.screeningNotFound(screeningId));
    }

    private Screening getScreeningOrThrowException(Long screeningId) {
        return screeningRepository
                .findById(screeningId)
                .orElseThrow(() -> ScreeningException.screeningNotFound(screeningId));
    }
}
