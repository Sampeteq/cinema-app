package code.screenings;

import code.screenings.dto.BookScreeningTicketDTO;
import code.screenings.dto.ScreeningTicketDTO;
import code.screenings.exception.ScreeningNotFoundException;
import code.screenings.exception.ScreeningSeatNotFoundException;
import code.screenings.exception.ScreeningTicketNotFoundException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.util.UUID;

@AllArgsConstructor
class ScreeningTicketBooker {

    private final ScreeningRepository screeningRepository;

    private final ScreeningTicketRepository screeningTicketRepository;

    ScreeningTicketDTO bookTicket(BookScreeningTicketDTO dto, Clock clock) {
        var screening = getScreeningOrThrow(dto.screeningId());
        var seat = screening
                .getSeat(dto.seatId())
                .orElseThrow(() -> new ScreeningSeatNotFoundException(dto.seatId()));
        var ticket = new ScreeningTicket(dto.firstName(), dto.lastName(), screening, seat);
        ticket.book(clock);
        return screeningTicketRepository
                .add(ticket)
                .toDTO();
    }

    void cancelTicket(UUID ticketId, Clock clock) {
        var ticket = getTicketOrThrow(ticketId);
        ticket.cancel(clock);
    }

    private Screening getScreeningOrThrow(UUID screeningId) {
        return screeningRepository
                .getById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));
    }

    private ScreeningTicket getTicketOrThrow(UUID ticketId) {
        return screeningTicketRepository
                .getById(ticketId)
                .orElseThrow(() -> new ScreeningTicketNotFoundException(ticketId));
    }
}
