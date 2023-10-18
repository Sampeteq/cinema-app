package com.cinema.rooms.domain.exceptions;

public class RoomsNoAvailableException extends RuntimeException {

    public RoomsNoAvailableException() {
        super("No available rooms");
    }
}
