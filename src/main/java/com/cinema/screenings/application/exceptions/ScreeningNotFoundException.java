package com.cinema.screenings.application.exceptions;

public class ScreeningNotFoundException extends RuntimeException {

    public ScreeningNotFoundException() {
        super("Screening not found");
    }
}
