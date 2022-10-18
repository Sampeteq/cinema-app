package code.screening.exception;

public class TooLateToBookTicketException extends TicketException {

    public TooLateToBookTicketException() {
        super("Too late to book ticket.");
    }
}
