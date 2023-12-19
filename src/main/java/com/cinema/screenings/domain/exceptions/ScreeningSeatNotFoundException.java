package com.cinema.screenings.domain.exceptions;

public class ScreeningSeatNotFoundException extends RuntimeException {

    public ScreeningSeatNotFoundException() {
        super("Seat not found");
    }
}
