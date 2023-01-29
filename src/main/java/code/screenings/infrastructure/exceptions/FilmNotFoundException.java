package code.screenings.infrastructure.exceptions;

import code.screenings.domain.exceptions.FilmException;

public class FilmNotFoundException extends FilmException {
    public FilmNotFoundException() {
        super("Film not found");
    }
}
