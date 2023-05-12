package code.user.domain.exceptions;

import code.shared.ValidationException;

public class UsernameAlreadyExistsException extends ValidationException {

    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
}
