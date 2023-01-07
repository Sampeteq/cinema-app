package code.screenings.exception;

import java.util.UUID;

public class FilmNotFoundException extends FilmException {
    public FilmNotFoundException(UUID filmId) {
        super("Film not found: " + filmId);
    }
}
