package code.catalog.domain.exceptions;

import code.shared.exceptions.ValidationException;

public class ScreeningDateOutOfRangeException extends ValidationException {

    private static final int MIN_BOOKING_DAYS_NUMBER = 7;
    private static final int MAX_BOOKING_DAYS_NUMBER = 21;

    public ScreeningDateOutOfRangeException() {
        super(
                "Difference between current and screening date " +
                "cannot be below " + MIN_BOOKING_DAYS_NUMBER +
                " and above " + MAX_BOOKING_DAYS_NUMBER + " days"
        );
    }
}
