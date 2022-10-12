package code.ticket.exception;

import java.util.UUID;

public class TicketNotFoundException extends TicketException {

    public TicketNotFoundException(Long ticketId) {
        super("Ticket not found: " + ticketId);
    }

    public TicketNotFoundException(UUID ticketId) {
        super("Ticket not found: " + ticketId);
    }
}
