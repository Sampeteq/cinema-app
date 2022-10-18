package code.screening.exception;

import java.util.UUID;

public class ScreeningTicketNotFoundException extends ScreeningException {

    public ScreeningTicketNotFoundException(Long ticketId) {
        super("Ticket not found: " + ticketId);
    }

    public ScreeningTicketNotFoundException(UUID ticketId) {
        super("Ticket not found: " + ticketId);
    }
}
