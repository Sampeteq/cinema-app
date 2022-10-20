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
}
