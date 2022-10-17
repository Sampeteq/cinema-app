package code.screening.exception;

public class ScreeningFreeSeatsQuantityException extends ScreeningException {

    public ScreeningFreeSeatsQuantityException(int minFreeSeats, int maxFreeSeats) {
        super("Wrong screening free seats quantity" +
                ".Min: " + minFreeSeats +
                ".Max: " + maxFreeSeats);
    }
}
