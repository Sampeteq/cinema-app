package com.cinema.screenings.domain.exceptions;

public class ScreeningsCollisionsException extends RuntimeException {

    public ScreeningsCollisionsException() {
        super("Screenings collisions");
    }
}
