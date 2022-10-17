package code.screening.exception;

public class ScreeningMinAgeException extends ScreeningException {

    public ScreeningMinAgeException(int minAge, int maxAge) {
        super(
                "Wrong screening min age exception"
                        + ".Min: " + minAge
                        + ".Max: " + maxAge
        );
    }
}
