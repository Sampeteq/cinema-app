package code.user.domain.exceptions;

import code.shared.ValidationException;

public class UserNotSamePasswordsException extends ValidationException {

    public UserNotSamePasswordsException() {
        super("Not same passwords");
    }
}
