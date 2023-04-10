package code.films.domain.exceptions;

import code.shared.ValidationException;

public class WrongFilmYearException extends ValidationException {

    public WrongFilmYearException() {
        super("A film year must be previous, current or next one");
    }
}
