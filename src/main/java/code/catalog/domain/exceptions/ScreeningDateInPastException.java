package code.catalog.domain.exceptions;

import code.shared.exceptions.ValidationException;

public class ScreeningDateInPastException extends ValidationException {

    public ScreeningDateInPastException() {
        super("Screening date cannot be in the past");
    }
}
