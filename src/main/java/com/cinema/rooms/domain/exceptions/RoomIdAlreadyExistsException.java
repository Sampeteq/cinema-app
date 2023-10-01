package com.cinema.rooms.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class RoomIdAlreadyExistsException extends ValidationException {

    public RoomIdAlreadyExistsException() {
        super("A room id already exists");
    }
}
