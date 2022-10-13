package code.screening.exception;

import code.screening.ScreeningValues;

public class ScreeningFreeSeatsQuantityException extends ScreeningException {

    public ScreeningFreeSeatsQuantityException() {
        super("Wrong screening free seats quantity.Min: "
                + ScreeningValues.SCREENING_MIN_FREE_SEATS
                + ".Max: "
                + ScreeningValues.SCREENING_MAX_FREE_SEATS);
    }
}
