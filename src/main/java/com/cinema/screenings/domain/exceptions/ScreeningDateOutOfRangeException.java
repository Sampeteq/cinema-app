package com.cinema.screenings.domain.exceptions;

public class ScreeningDateOutOfRangeException extends RuntimeException {

    private static final int MIN_DAYS_NUMBER = 7;
    private static final int MAX_DAYS_NUMBER = 21;

    public ScreeningDateOutOfRangeException() {
        super(
                "Difference between current and screening screeningDate " +
                "cannot be below " + MIN_DAYS_NUMBER +
                " and above " + MAX_DAYS_NUMBER + " days"
        );
    }
}
