package com.example.screening.domain.exception;

import com.example.screening.domain.ScreeningId;

public class NoScreeningFreeSeatsException extends ScreeningException {

    public NoScreeningFreeSeatsException(ScreeningId screeningId) {
        super("No screening free seats: " + screeningId);
    }
}
