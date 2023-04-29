package code.user.client.exceptions;

import code.shared.ValidationException;

public class NotSamePasswordsException extends ValidationException {

    public NotSamePasswordsException() {
        super("Not same passwords");
    }
}
