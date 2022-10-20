package code.reservation.infrastructure;

import code.reservation.ReservationFacade;
import code.reservation.dto.ReserveScreeningTicketDTO;
import code.reservation.dto.TicketDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
class ReservationController {

    private final ReservationFacade reservationFacade;

    private final Clock clock = Clock.systemUTC();

    @PostMapping("/screenings-tickets")
    TicketDTO reserveTicket(@RequestBody @Valid ReserveScreeningTicketDTO dto) {
        return reservationFacade.reserveTicket(dto, clock);
    }

    @PatchMapping("/screenings-tickets/{ticketUUID}/cancelled")
    void cancelTicket(@PathVariable UUID ticketUUID) {
        reservationFacade.cancelTicket(ticketUUID, clock);
    }

    @GetMapping("/screenings-tickets")
    List<TicketDTO> readAllTickets() {
        return reservationFacade.readAllTickets();
    }
}
