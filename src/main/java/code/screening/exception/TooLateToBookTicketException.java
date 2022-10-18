package code.screening.exception;

public class TooLateToBookTicketException extends ScreeningException {

    public TooLateToBookTicketException() {
        super("Too late to book ticket.");
    }
}
