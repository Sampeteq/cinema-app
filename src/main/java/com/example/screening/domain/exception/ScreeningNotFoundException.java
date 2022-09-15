package com.example.screening.domain.exception;

import com.example.screening.domain.ScreeningId;

public class ScreeningNotFoundException extends ScreeningException {

    public ScreeningNotFoundException(ScreeningId screeningId) {
        super("Screening not found: " + screeningId);
    }
}
