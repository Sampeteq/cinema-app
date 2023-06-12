package code.films.domain.exceptions;

import code.shared.ValidationException;

public class ScreeningWrongDateException extends ValidationException {

    public ScreeningWrongDateException() {
        super("A screening date year must be current or next one");
    }
}
