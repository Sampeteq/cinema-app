package code.films.domain.exceptions;

import code.shared.ValidationException;

public class FilmScreeningWrongDateException extends ValidationException {

    public FilmScreeningWrongDateException() {
        super("A screening date year must be current or next one");
    }
}
