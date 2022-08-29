package com.example.screening.domain.exception;

import java.util.UUID;

public class ScreeningNotFoundException extends RuntimeException {

    public ScreeningNotFoundException(UUID screeningId) {
        super("Screening not found: " + screeningId);
    }
}
