package com.cinema.screenings.exceptions;

public class ScreeningNotFoundException extends RuntimeException {

    public ScreeningNotFoundException() {
        super("Screening not found");
    }
}
