package code.screening.exception;

public class ScreeningNoFreeSeatsException extends ScreeningException {

    public ScreeningNoFreeSeatsException(Long screeningId) {
        super("No free seats for screening: " + screeningId);
    }
}
