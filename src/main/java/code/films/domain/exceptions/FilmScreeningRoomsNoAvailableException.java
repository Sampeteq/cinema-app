package code.films.domain.exceptions;

import code.shared.ValidationException;

public class FilmScreeningRoomsNoAvailableException extends ValidationException {

    public FilmScreeningRoomsNoAvailableException() {
        super("No available rooms");
    }
}
