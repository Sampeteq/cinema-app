package code.catalog.domain.exceptions;

import code.shared.ValidationException;

public class FilmWrongYearException extends ValidationException {

    public FilmWrongYearException() {
        super("A film year must be previous, current or next one");
    }
}
