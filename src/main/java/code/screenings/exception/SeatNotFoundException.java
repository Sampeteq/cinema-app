package code.screenings.exception;

import java.util.UUID;

public class SeatNotFoundException extends ScreeningException {

    public SeatNotFoundException(UUID seatId) {
        super("Screening seat not found:" + seatId);
    }
}
