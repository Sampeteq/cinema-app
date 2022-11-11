package code.screenings;

import code.films.FilmFacade;
import code.films.exception.FilmNotFoundException;
import code.screenings.dto.*;
import code.screenings.exception.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class ScreeningFacade {

    private final ScreeningRepository screeningRepository;

    private final ScreeningRoomRepository screeningRoomRepository;

    private final ScreeningTicketRepository screeningTicketRepository;

    private final FilmFacade filmFacade;

    public ScreeningDTO add(AddScreeningDTO dto) {
        var date = ScreeningDate.of(dto.date());
        var room = getScreeningRoomOrThrow(dto.roomId());
        validateScreeningRoomBeingBusy(date, dto.roomId());
        validateFilmExisting(dto.filmId());
        var screening = Screening.of(
                date,
                dto.minAge(),
                dto.freeSeatsQuantity(),
                dto.filmId(),
                room
        );
        return screeningRepository
                .save(screening)
                .toDTO();
    }

    public ScreeningDTO read(UUID screeningId) {
        return getScreeningOrThrow(screeningId).toDTO();
    }

    public List<ScreeningDTO> searchBy(Map<String, Object> readParams) {
        var filmId = (UUID) readParams.get("filmId");
        var date = (LocalDateTime) readParams.get("date");
        var screeningBuilder = Screening
                .builder()
                .filmId(filmId);
        if (date != null) {
            var screeningDate = ScreeningDate.of(date);
            screeningBuilder.date(screeningDate);
        }
        var screening = screeningBuilder.build();
        var example = Example.of(screening);

        return screeningRepository
                .findAll(example)
                .stream()
                .map(Screening::toDTO)
                .toList();
    }

    public ScreeningRoomDTO addRoom(AddScreeningRoomDTO dto) {
        if (screeningRoomRepository.existsByNumber(dto.number())) {
            throw new ScreeningRoomAlreadyExistsException(dto.number());
        }
        var screeningRoom = ScreeningRoom
                .builder()
                .number(dto.number())
                .freeSeats(dto.freeSeats())
                .build();
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

    @Transactional
    public TicketDTO bookTicket(BookScreeningTicketDTO dto, Clock clock) {
        var screening = getScreeningOrThrow(dto.screeningId());
        var ticket = new ScreeningTicket(dto.firstName(), dto.lastName(), screening);
        ticket.book(clock);
        return screeningTicketRepository
                .save(ticket)
                .toDTO();
    }

    @Transactional
    public void cancelTicket(UUID ticketId, Clock clock) {
        var ticket = getTicketOrThrow(ticketId);
        ticket.cancel(clock);
    }

    public TicketDTO readTicket(UUID ticketId) {
        return getTicketOrThrow(ticketId).toDTO();
    }

    public List<TicketDTO> readAllTickets() {
        return screeningTicketRepository
                .findAll()
                .stream()
                .map(ScreeningTicket::toDTO)
                .toList();
    }

    private Screening getScreeningOrThrow(UUID screeningId) {
        return screeningRepository
                .findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));
    }

    private void validateFilmExisting(UUID filmId) {
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
        if (screeningRepository.existsByDateAndRoom_id(date, roomUuid)) {
            throw new ScreeningRoomBusyException(roomUuid);
        }
    }

    private ScreeningTicket getTicketOrThrow(UUID ticketId) {
        return screeningTicketRepository
                .findById(ticketId)
                .orElseThrow(() -> new ScreeningTicketNotFoundException(ticketId));
    }
}
