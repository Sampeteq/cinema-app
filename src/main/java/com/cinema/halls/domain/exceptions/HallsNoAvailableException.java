package com.cinema.halls.domain.exceptions;

public class HallsNoAvailableException extends RuntimeException {

    public HallsNoAvailableException() {
        super("No available halls");
    }
}
