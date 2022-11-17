package code.screenings.exception;

import java.util.UUID;

public class ScreeningSeatNotFoundException extends ScreeningException {

    public ScreeningSeatNotFoundException(UUID seatId) {
        super("Screening seat not found:" + seatId);
    }
}
