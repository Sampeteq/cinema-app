package code.screening.exception;

public class ScreeningRoomAlreadyExistsException extends ScreeningException {

    public ScreeningRoomAlreadyExistsException(int number) {
        super("A screening room with this number already exists: " + number);
    }
}
