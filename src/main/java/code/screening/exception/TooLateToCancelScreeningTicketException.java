package code.screening.exception;

public class TooLateToCancelScreeningTicketException extends ScreeningException {

    public TooLateToCancelScreeningTicketException() {
        super("Too late to cancel ticket reservation.");
    }
}
