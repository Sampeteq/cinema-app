package code.user.domain.exceptions;

import code.shared.ValidationException;

public class UserMailAlreadyExistsException extends ValidationException {

    public UserMailAlreadyExistsException() {
        super("User with this email already exists");
    }
}
