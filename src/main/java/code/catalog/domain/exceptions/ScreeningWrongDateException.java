package code.catalog.domain.exceptions;

import code.shared.exceptions.ValidationException;

public class ScreeningWrongDateException extends ValidationException {

    public ScreeningWrongDateException() {
        super("A film date year must be current or next one");
    }
}
