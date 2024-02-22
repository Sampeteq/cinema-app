package com.cinema.halls.exceptions;

public class HallNotFoundException extends RuntimeException {

    public HallNotFoundException() {
        super("Hall not found");
    }
}
