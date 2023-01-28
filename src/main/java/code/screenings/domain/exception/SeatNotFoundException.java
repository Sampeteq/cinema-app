package code.screenings.domain.exception;

public class SeatNotFoundException extends ScreeningException {

    public SeatNotFoundException() {
        super("Screening seat not found");
    }
}
