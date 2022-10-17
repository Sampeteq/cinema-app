package code.screening.exception;

public class ScreeningAgeException extends ScreeningException {

    public ScreeningAgeException(int minAge, int maxAge) {
        super(
                "Wrong screening min age exception"
                        + ".Min: " + minAge
                        + ".Max: " + maxAge
        );
    }
}
