package com.cinema.rooms.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class RoomCustomIdAlreadyExistsException extends ValidationException {

    public RoomCustomIdAlreadyExistsException() {
        super("A room customId already exists");
    }
}