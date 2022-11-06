package code.bookings;

import code.bookings.dto.BookScreeningTicketDTO;
import code.bookings.dto.TicketDTO;
import code.bookings.exception.ScreeningTicketNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class BookingFacade {

    private final ScreeningTicketRepository screeningTicketRepository;

    private final ScreeningTicketBookingService screeningTicketBookingService;

    @Transactional
    public TicketDTO bookTicket(BookScreeningTicketDTO dto, Clock clock) {
        var ticket = screeningTicketBookingService.book(dto, clock);
        return screeningTicketRepository
                .save(ticket)
                .toDTO();
    }

    @Transactional
    public void cancelTicket(UUID ticketId, Clock clock) {
        var ticket = getTicketOrThrow(ticketId);
        screeningTicketBookingService.cancel(ticket, clock);
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

    private ScreeningTicket getTicketOrThrow(UUID ticketId) {
        return screeningTicketRepository
                .findByUuid(ticketId)
                .orElseThrow(() -> new ScreeningTicketNotFoundException(ticketId));
    }
}
