package code.screenings.infrastructure.exceptions;

import code.screenings.domain.exceptions.ScreeningException;

public class ScreeningNotFoundException extends ScreeningException {

    public ScreeningNotFoundException() {
        super("Screening not found");
    }
}
