package code.bookings.infrastructure.exceptions;

import code.bookings.domain.exceptions.FilmException;

public class FilmNotFoundException extends FilmException {
    public FilmNotFoundException() {
        super("Film not found");
    }
}
