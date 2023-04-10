package code.user.domain.exceptions;

import code.shared.ValidationException;

public class NotSamePasswordsException extends ValidationException {

    public NotSamePasswordsException() {
        super("Not same passwords");
    }
}
