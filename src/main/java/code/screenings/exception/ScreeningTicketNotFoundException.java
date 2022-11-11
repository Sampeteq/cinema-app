package code.screenings.exception;

import java.util.UUID;

public class ScreeningTicketNotFoundException extends BookingException {

    public ScreeningTicketNotFoundException(UUID ticketId) {
        super("Ticket not found: " + ticketId);
    }
}
