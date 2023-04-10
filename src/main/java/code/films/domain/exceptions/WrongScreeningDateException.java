package code.films.domain.exceptions;

import code.shared.ValidationException;

public class WrongScreeningDateException extends ValidationException {

    public WrongScreeningDateException() {
        super("A screening date year must be current or next one");
    }
}
