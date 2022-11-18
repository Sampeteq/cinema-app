package code.screenings;

import code.screenings.dto.*;
import code.screenings.exception.*;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class ScreeningFacade {

    private final ScreeningRepository screeningRepository;

    private final ScreeningRoomRepository screeningRoomRepository;

    private final ScreeningTicketRepository screeningTicketRepository;

    private final ScreeningSearcher screeningSearcher;

    private final ScreeningCreator screeningCreator;

    private final ScreeningTicketBooker screeningTicketBooker;

    public ScreeningDTO add(AddScreeningDTO dto) {
        return screeningCreator.add(dto);
    }

    public ScreeningDTO read(UUID screeningId) {
        return getScreeningOrThrow(screeningId).toDTO();
    }

    public List<ScreeningDTO> searchBy(Map<String, Object> params) {
        return screeningSearcher.searchBy(params);
    }

    public ScreeningRoomDTO addRoom(AddScreeningRoomDTO dto) {
        if (screeningRoomRepository.existsByNumber(dto.number())) {
            throw new ScreeningRoomAlreadyExistsException(dto.number());
        }
        var screeningRoom = ScreeningRoom
                .builder()
                .id(UUID.randomUUID())
                .number(dto.number())
                .rowsQuantity(dto.rowsQuantity())
                .seatsInOneRowQuantity(dto.seatsQuantityInOneRow())
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
        return screeningTicketBooker.bookTicket(dto, clock);
    }

    @Transactional
    public void cancelTicket(UUID ticketId, Clock clock) {
        screeningTicketBooker.cancelTicket(ticketId, clock);
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

    private ScreeningTicket getTicketOrThrow(UUID ticketId) {
        return screeningTicketRepository
                .findById(ticketId)
                .orElseThrow(() -> new ScreeningTicketNotFoundException(ticketId));
    }
}
