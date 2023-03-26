package code.bookings.infrastructure.exceptions;

import code.bookings.domain.exceptions.ScreeningException;

public class ScreeningNotFoundException extends ScreeningException {

    public ScreeningNotFoundException() {
        super("Screening not found");
    }
}
