package com.cinema.screenings.domain.exceptions;

public class SeatNotFoundException extends RuntimeException {

    public SeatNotFoundException() {
        super("Seat not found");
    }
}
