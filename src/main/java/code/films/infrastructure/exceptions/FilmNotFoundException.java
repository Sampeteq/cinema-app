package code.films.infrastructure.exceptions;

import code.films.domain.exceptions.FilmException;

public class FilmNotFoundException extends FilmException {
    public FilmNotFoundException() {
        super("Film not found");
    }
}
