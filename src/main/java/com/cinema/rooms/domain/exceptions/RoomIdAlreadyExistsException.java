package com.cinema.rooms.domain.exceptions;

public class RoomIdAlreadyExistsException extends RuntimeException {

    public RoomIdAlreadyExistsException() {
        super("A room id already exists");
    }
}
