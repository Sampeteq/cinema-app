package code.screening.exception;

public class ScreeningRoomAlreadyExistsException extends ScreeningException {

    public ScreeningRoomAlreadyExistsException(int roomNumber) {
        super("Screening room already exists: " + roomNumber);
    }
}
