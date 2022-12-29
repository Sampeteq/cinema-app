package code.screenings.exception;

import java.util.UUID;

public class SeatBusyException extends ScreeningException {

    public SeatBusyException(UUID seatId) {
        super("Screening seat is busy: " + seatId);
    }
}
