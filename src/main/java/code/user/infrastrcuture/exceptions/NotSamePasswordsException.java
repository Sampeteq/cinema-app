package code.user.infrastrcuture.exceptions;

import code.user.domain.exception.UserException;

public class NotSamePasswordsException extends UserException {

    public NotSamePasswordsException() {
        super("Not same passwords");
    }
}
