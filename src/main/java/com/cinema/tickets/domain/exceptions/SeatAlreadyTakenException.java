package com.cinema.tickets.domain.exceptions;

public class SeatAlreadyTakenException extends RuntimeException {

    public SeatAlreadyTakenException() {
        super("Seat already taken");
    }
}
