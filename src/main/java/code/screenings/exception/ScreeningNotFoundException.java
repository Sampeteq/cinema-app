package code.screenings.exception;

import java.util.UUID;

public class ScreeningNotFoundException extends ScreeningException {

    public ScreeningNotFoundException(UUID screeningId) {
        super("Screening not found: " + screeningId);
    }
}
