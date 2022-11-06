package code.bookings.exception;

import java.util.UUID;

public class ScreeningTicketNotFoundException extends BookingException {

    public ScreeningTicketNotFoundException(UUID ticketUUID) {
        super("Ticket not found: " + ticketUUID);
    }
}
