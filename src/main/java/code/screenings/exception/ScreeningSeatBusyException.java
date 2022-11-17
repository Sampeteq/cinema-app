package code.screenings.exception;

import java.util.UUID;

public class ScreeningSeatBusyException extends ScreeningException {

    public ScreeningSeatBusyException(UUID seatId) {
        super("Screening seat is busy: " + seatId);
    }
}
