package code.catalog.domain.exceptions;

import code.shared.exceptions.ValidationException;

public class FilmYearOutOfRangeException extends ValidationException {

    public FilmYearOutOfRangeException() {
        super("A film year must be previous, current or next one");
    }
}
