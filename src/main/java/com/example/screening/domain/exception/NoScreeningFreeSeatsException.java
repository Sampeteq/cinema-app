package com.example.screening.domain.exception;

import java.util.UUID;

public class NoScreeningFreeSeatsException extends RuntimeException {

    public NoScreeningFreeSeatsException(UUID screeningId) {
        super("No screening free seats: " + screeningId);
    }
}
