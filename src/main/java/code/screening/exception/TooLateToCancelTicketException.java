package code.screening.exception;

public class TooLateToCancelTicketException extends TicketException {

    public TooLateToCancelTicketException() {
        super("Too late to cancel ticket reservation.");
    }
}
