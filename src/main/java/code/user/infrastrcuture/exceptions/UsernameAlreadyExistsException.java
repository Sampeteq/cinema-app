package code.user.infrastrcuture.exceptions;

import code.user.domain.exceptions.UserException;

public class UsernameAlreadyExistsException extends UserException {

    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
}
