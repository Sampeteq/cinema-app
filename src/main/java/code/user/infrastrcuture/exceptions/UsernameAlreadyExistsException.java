package code.user.infrastrcuture.exceptions;

import code.user.domain.exception.UserException;

public class UsernameAlreadyExistsException extends UserException {

    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
}
