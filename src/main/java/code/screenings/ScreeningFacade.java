package code.screenings;

import code.screenings.dto.*;
import code.screenings.exception.ScreeningRoomAlreadyExistsException;
import code.screenings.exception.ScreeningTicketNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class ScreeningFacade {

    private final ScreeningRoomRepository screeningRoomRepository;

    private final ScreeningTicketRepository screeningTicketRepository;

    private final ScreeningSearcher screeningSearcher;

    private final ScreeningCreator screeningCreator;

    private final ScreeningTicketBooker screeningTicketBooker;

    public ScreeningDTO add(AddScreeningDTO dto) {
        return screeningCreator.add(dto);
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
                .add(screeningRoom)
                .toDTO();
    }

    public List<ScreeningRoomDTO> readAllRooms() {
        return screeningRoomRepository
                .getAll()
                .stream()
                .map(ScreeningRoom::toDTO)
                .toList();
    }

    @Transactional
    public ScreeningTicketDTO bookTicket(BookScreeningTicketDTO dto, Clock clock) {
        return screeningTicketBooker.bookTicket(dto, clock);
    }

    @Transactional
    public void cancelTicket(UUID ticketId, Clock clock) {
        screeningTicketBooker.cancelTicket(ticketId, clock);
    }

    public ScreeningTicketDTO readTicket(UUID ticketId) {
        return getTicketOrThrow(ticketId).toDTO();
    }

    public List<ScreeningTicketDTO> readAllTickets() {
        return screeningTicketRepository
                .getAll()
                .stream()
                .map(ScreeningTicket::toDTO)
                .toList();
    }

    private ScreeningTicket getTicketOrThrow(UUID ticketId) {
        return screeningTicketRepository
                .getById(ticketId)
                .orElseThrow(() -> new ScreeningTicketNotFoundException(ticketId));
    }
}
