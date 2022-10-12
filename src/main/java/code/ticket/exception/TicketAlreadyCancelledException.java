package code.ticket.exception;

import java.util.UUID;

public class TicketAlreadyCancelledException extends TicketException {

    public TicketAlreadyCancelledException(UUID ticketId) {
        super("Ticket already cancelled: " + ticketId);
    }
}
