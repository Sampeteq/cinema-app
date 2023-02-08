package code.user.infrastrcuture.exceptions;

import code.user.domain.exceptions.UserException;

public class NotSamePasswordsException extends UserException {

    public NotSamePasswordsException() {
        super("Not same passwords");
    }
}
