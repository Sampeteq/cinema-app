package code.films.domain.exceptions;

import code.shared.ValidationException;

public class FilmScreeningRoomCustomIdAlreadyExistsException extends ValidationException {

    public FilmScreeningRoomCustomIdAlreadyExistsException() {
        super("A room customId already exists");
    }
}
