package code.user.domain.exception;

public abstract class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }
}
