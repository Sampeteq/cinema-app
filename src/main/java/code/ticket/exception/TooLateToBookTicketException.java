package code.ticket.exception;

public class TooLateToBookTicketException extends TicketException {

    public TooLateToBookTicketException() {
        super("Too late to book ticket.");
    }
}
