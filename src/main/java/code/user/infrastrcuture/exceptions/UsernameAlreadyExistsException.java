package code.user.infrastrcuture.exceptions;

import code.shared.ValidationException;

public class UsernameAlreadyExistsException extends ValidationException {

    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
}
