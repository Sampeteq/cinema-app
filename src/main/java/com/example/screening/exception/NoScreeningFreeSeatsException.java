package com.example.screening.exception;

public class NoScreeningFreeSeatsException extends ScreeningException {

    public NoScreeningFreeSeatsException(Long screeningId) {
        super("No screening free seats: " + screeningId);
    }
}
