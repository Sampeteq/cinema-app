package code.screenings.exception;

import java.util.UUID;

public class ScreeningNoFreeSeatsException extends ScreeningException {

    public ScreeningNoFreeSeatsException(UUID screeningId) {
        super("No free seats for screening: " + screeningId);
    }
}
