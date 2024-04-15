package com.cinema.halls.application.exceptions;

public class HallNotFoundException extends RuntimeException {

    public HallNotFoundException() {
        super("Hall not found");
    }
}
