package code.screenings.domain.exceptions;

import code.shared.ValidationException;

public class NoAvailableRoomsException extends ValidationException {

    public NoAvailableRoomsException() {
        super("No available rooms");
    }
}
