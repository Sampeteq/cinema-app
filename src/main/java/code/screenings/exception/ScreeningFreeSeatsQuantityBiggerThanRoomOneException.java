package code.screenings.exception;

public class ScreeningFreeSeatsQuantityBiggerThanRoomOneException extends ScreeningException {

    public ScreeningFreeSeatsQuantityBiggerThanRoomOneException() {
        super("Screening free seats quantity bigger than room one.");
    }
}
