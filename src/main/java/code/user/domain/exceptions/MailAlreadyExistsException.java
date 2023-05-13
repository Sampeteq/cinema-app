package code.user.domain.exceptions;

import code.shared.ValidationException;

public class MailAlreadyExistsException extends ValidationException {

    public MailAlreadyExistsException() {
        super("User with this email already exists");
    }
}
