package com.example.screening.domain.exception;

public class ScreeningNotFoundException extends ScreeningException {

    public ScreeningNotFoundException(Long screeningId) {
        super("Screening not found: " + screeningId);
    }
}
