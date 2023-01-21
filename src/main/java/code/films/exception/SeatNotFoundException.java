package code.films.exception;

public class SeatNotFoundException extends ScreeningException {

    public SeatNotFoundException() {
        super("Screening seat not found");
    }
}
