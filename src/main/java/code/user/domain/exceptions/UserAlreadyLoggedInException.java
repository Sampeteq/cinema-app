package code.user.domain.exceptions;

import code.shared.ValidationException;

public class UserAlreadyLoggedInException extends ValidationException {

    public UserAlreadyLoggedInException() {
        super("User already logged in");
    }
}
