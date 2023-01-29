package code.screenings.infrastructure.exceptions;

import code.screenings.domain.exceptions.ScreeningException;

public class SeatNotFoundException extends ScreeningException {

    public SeatNotFoundException() {
        super("Seat not found");
    }
}
