package code.screening.exception;

import java.util.UUID;

public class ScreeningTicketAlreadyCancelledException extends ScreeningException {

    public ScreeningTicketAlreadyCancelledException(UUID ticketId) {
        super("Ticket already cancelled: " + ticketId);
    }
}
