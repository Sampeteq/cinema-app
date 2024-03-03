package com.cinema.halls.domain.exceptions;

public class HallNotFoundException extends RuntimeException {

    public HallNotFoundException() {
        super("Hall not found");
    }
}
