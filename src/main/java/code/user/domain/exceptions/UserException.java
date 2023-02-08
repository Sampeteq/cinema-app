package code.user.domain.exceptions;

public abstract class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }
}
