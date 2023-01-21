package code.films.exception;

public class ScreeningRoomNotFoundException extends ScreeningException {

    public ScreeningRoomNotFoundException() {
        super("Screening room not found");
    }
}
