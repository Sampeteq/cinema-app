package code.user.domain.exceptions;

import code.shared.exceptions.ValidationException;

public class UserNotSamePasswordsException extends ValidationException {

    public UserNotSamePasswordsException() {
        super("Not same passwords");
    }
}
