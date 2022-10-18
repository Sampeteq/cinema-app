package code.screening.exception;

public class TooLateToBookScreeningTicketException extends ScreeningException {

    public TooLateToBookScreeningTicketException() {
        super("Too late to book ticket.");
    }
}
