package code.screening.exception;

public class ScreeningFreeSeatsException extends ScreeningException {

    private ScreeningFreeSeatsException(String message, ScreeningExceptionType type) {
        super(message, type);
    }

    public static ScreeningFreeSeatsException noFreeSeats(Long screeningId) {
        return new ScreeningFreeSeatsException(
                "No free seats for screening: " + screeningId, ScreeningExceptionType.NO_FREE_SEATS
        );
    }

    public static ScreeningFreeSeatsException wrongQuantity(int minFreeSeats, int maxFreeSeats) {
        return new ScreeningFreeSeatsException(
                "Wrong screening free seats quantity" +
                        ".Min: " + minFreeSeats +
                        ".Max: " + maxFreeSeats, ScreeningExceptionType.WRONG_FREE_SEATS_QUANTITY
        );
    }
}
