package code.screening.exception;

public class TooLateToCancelTicketException extends ScreeningException {

    public TooLateToCancelTicketException() {
        super("Too late to cancel ticket reservation.");
    }
}
