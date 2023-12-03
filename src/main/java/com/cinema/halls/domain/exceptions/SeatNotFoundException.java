package com.cinema.halls.domain.exceptions;

public class SeatNotFoundException extends RuntimeException {

    public SeatNotFoundException() {
        super("Seat not found");
    }
}
