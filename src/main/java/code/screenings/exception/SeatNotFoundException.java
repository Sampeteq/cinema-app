package code.screenings.exception;

public class SeatNotFoundException extends ScreeningException {

    public SeatNotFoundException() {
        super("Screening seat not found");
    }
}
