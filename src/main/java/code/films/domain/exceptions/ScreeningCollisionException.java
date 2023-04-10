package code.films.domain.exceptions;

import code.shared.ValidationException;

public class ScreeningCollisionException extends ValidationException {

    public ScreeningCollisionException() {
        super("Time and room collision between screenings");
    }
}
