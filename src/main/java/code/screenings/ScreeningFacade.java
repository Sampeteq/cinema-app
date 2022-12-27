package code.screenings;

import code.screenings.dto.*;
import code.screenings.exception.ScreeningTicketNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ScreeningFacade {

    private final ScreeningRoomRepository screeningRoomRepository;

    private final ScreeningTicketRepository screeningTicketRepository;

    private final ScreeningSearcher screeningSearcher;

    private final ScreeningRoomCreator screeningRoomCreator;

    private final ScreeningFactory screeningFactory;

    private final ScreeningTicketBooker screeningTicketBooker;

    private final ScreeningRepository screeningRepository;

    @Transactional
    public ScreeningDto add(AddScreeningDto dto) {
        var screening = screeningFactory.createScreening(
                dto.date(),
                dto.minAge(),
                dto.filmId(),
                dto.roomId()
        );
        return screeningRepository
                .add(screening)
                .toDTO();
    }

    @Transactional
    public List<ScreeningDto> searchBy(ScreeningSearchParamsDto paramsDto) {
        return screeningSearcher.searchBy(paramsDto);
    }

    @Transactional
    public ScreeningRoomDto addRoom(AddScreeningRoomDto dto) {
        return screeningRoomCreator.addRoom(dto);
    }

    public List<ScreeningRoomDto> readAllRooms() {
        return screeningRoomRepository
                .getAll()
                .stream()
                .map(ScreeningRoom::toDTO)
                .toList();
    }

    @Transactional
    public ScreeningTicketDto bookTicket(BookScreeningTicketDto dto, Clock clock) {
        return screeningTicketBooker.bookTicket(dto, clock);
    }

    @Transactional
    public void cancelTicket(UUID ticketId, Clock clock) {
        screeningTicketBooker.cancelTicket(ticketId, clock);
    }

    public ScreeningTicketDto readTicket(UUID ticketId) {
        return getTicketOrThrow(ticketId).toDTO();
    }

    public List<ScreeningTicketDto> readAllTickets() {
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
