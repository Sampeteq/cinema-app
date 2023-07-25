package code.catalog.domain.exceptions;

import code.shared.exceptions.ValidationException;

public class RoomsNoAvailableException extends ValidationException {

    public RoomsNoAvailableException() {
        super("No available rooms");
    }
}
