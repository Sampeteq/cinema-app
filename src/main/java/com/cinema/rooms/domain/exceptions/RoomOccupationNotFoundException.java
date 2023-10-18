package com.cinema.rooms.domain.exceptions;

public class RoomOccupationNotFoundException extends RuntimeException {

    public RoomOccupationNotFoundException() {
        super("Room occupation not found");
    }
}
