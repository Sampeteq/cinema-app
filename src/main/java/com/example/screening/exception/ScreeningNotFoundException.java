package com.example.screening.exception;

public class ScreeningNotFoundException extends ScreeningException {

    public ScreeningNotFoundException(Long screeningId) {
        super("Screening not found: " + screeningId);
    }
}
