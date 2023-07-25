package code.user.domain.exceptions;

import code.shared.exceptions.ValidationException;

public class UserMailAlreadyExistsException extends ValidationException {

    public UserMailAlreadyExistsException() {
        super("User with this email already exists");
    }
}
