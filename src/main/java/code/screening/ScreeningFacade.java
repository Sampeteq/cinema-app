package code.screening;

import code.film.FilmFacade;
import code.film.exception.FilmNotFoundException;
import code.screening.dto.*;
import code.screening.exception.ScreeningNotFoundException;
import code.screening.exception.ScreeningRoomAlreadyExistsException;
import code.screening.exception.ScreeningRoomNotFoundException;
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
                .orElseThrow(() -> new ScreeningRoomNotFoundException(dto.roomUuid()));

        var screening = new Screening(
                ScreeningDate.of(dto.date(), currentYear),
                ScreeningAge.of(dto.minAge()),
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

    public void decreaseFreeSeatsByOne(Long screeningId) {
        getScreeningOrThrowException(screeningId).decreaseFreeSeatsByOne();
    }

    public ScreeningTicketDTO readScreeningTicketData(Long screeningId) {
        var screening = getScreeningOrThrowException(screeningId).toDTO();
        return new ScreeningTicketDTO(screening.date(), screening.freeSeats());
    }

    public ScreeningRoomDTO addRoom(AddScreeningRoomDTO dto) {
        if (screeningRoomRepository.existsByNumber(dto.number())) {
            throw new ScreeningRoomAlreadyExistsException(dto.number());
        }
        var screeningRoom = new ScreeningRoom(dto.number(), ScreeningRoomFreeSeats.of(dto.freeSeats()));
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

    private Screening getScreeningOrThrowException(Long screeningId) {
        return screeningRepository
                .findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));
    }
}
