package code.screening.exception;

import java.util.UUID;

public class TicketAlreadyCancelledException extends ScreeningException {

    public TicketAlreadyCancelledException(UUID ticketId) {
        super("Ticket already cancelled: " + ticketId);
    }
}
