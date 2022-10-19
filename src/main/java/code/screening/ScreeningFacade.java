package code.screening;

import code.film.FilmFacade;
import code.film.exception.FilmNotFoundException;
import code.screening.dto.*;
import code.screening.exception.*;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ScreeningFacade {

    private final ScreeningRepository screeningRepository;

    private final ScreeningRoomRepository screeningRoomRepository;

    private final ScreeningTicketRepository screeningTicketRepository;
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

    public ScreeningRoomDTO addRoom(AddScreeningRoomDTO dto) {
        if (screeningRoomRepository.existsByNumber(dto.number())) {
            throw ScreeningRoomException.alreadyExists(dto.number());
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
                .orElseThrow(() -> ScreeningRoomException.notFound(roomUuid));
    }

    public List<ScreeningRoomDTO> readAllRooms() {
        return screeningRoomRepository
                .findAll()
                .stream()
                .map(ScreeningRoom::toDTO)
                .toList();
    }

    @Transactional
    public TicketDTO reserveTicket(ReserveScreeningTicketDTO dto, Clock clock) {
        var screening= getScreeningOrThrowException(dto.screeningId());
        var screeningTicket = new ScreeningTicket(dto.firstName(), dto.lastName(), screening);
//        screening.bookTicket(screeningTicket, clock);
        screeningTicket.reserve(clock);
        return screeningTicketRepository.save(screeningTicket).toDTO();
//        return screeningTicket.toDTO();
    }

    @Transactional
    public void cancelTicket(UUID ticketId, Clock clock) {
        var ticket = screeningTicketRepository
                .findByUuid(ticketId)
                .orElseThrow(() -> ScreeningTicketException.notFound(ticketId));
        ticket.cancel(clock);
    }

    public TicketDTO readTicket(UUID ticketId) {
        return getTicketOrThrowException(ticketId).toDTO();
    }

    public List<TicketDTO> readAllTickets() {
        return screeningTicketRepository
                .findAll()
                .stream()
                .map(ScreeningTicket::toDTO)
                .toList();
    }

    private Screening getScreeningOrThrowException(Long screeningId) {
        return screeningRepository
                .findById(screeningId)
                .orElseThrow(() -> ScreeningException.screeningNotFound(screeningId));
    }

    private ScreeningTicket getTicketOrThrowException(UUID ticketId) {
        return screeningTicketRepository
                .findByUuid(ticketId)
                .orElseThrow(() -> ScreeningTicketException.notFound(ticketId));
    }
}
