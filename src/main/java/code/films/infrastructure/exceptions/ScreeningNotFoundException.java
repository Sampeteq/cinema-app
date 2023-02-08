package code.films.infrastructure.exceptions;

import code.films.domain.exceptions.ScreeningException;

public class ScreeningNotFoundException extends ScreeningException {

    public ScreeningNotFoundException() {
        super("Screening not found");
    }
}
