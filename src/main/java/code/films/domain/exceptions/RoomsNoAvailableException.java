package code.films.domain.exceptions;

import code.shared.ValidationException;

public class RoomsNoAvailableException extends ValidationException {

    public RoomsNoAvailableException() {
        super("No available rooms");
    }
}
