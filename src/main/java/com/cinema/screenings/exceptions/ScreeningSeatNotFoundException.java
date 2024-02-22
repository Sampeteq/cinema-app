package com.cinema.screenings.exceptions;

public class ScreeningSeatNotFoundException extends RuntimeException {

    public ScreeningSeatNotFoundException() {
        super("Seat not found");
    }
}
