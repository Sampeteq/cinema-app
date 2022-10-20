package code.reservation.exception;

import java.util.UUID;

public class ScreeningTicketNotFoundException extends ReservationException {

    public ScreeningTicketNotFoundException(UUID ticketUUID) {
        super("Ticket not found: " + ticketUUID);
    }
}
