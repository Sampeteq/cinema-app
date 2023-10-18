package com.cinema.rooms.domain.exceptions;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException() {
        super("Room not found");
    }
}
