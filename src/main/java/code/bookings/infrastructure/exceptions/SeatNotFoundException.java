package code.bookings.infrastructure.exceptions;

import code.bookings.domain.exceptions.ScreeningException;

public class SeatNotFoundException extends ScreeningException {

    public SeatNotFoundException() {
        super("Seat not found");
    }
}
