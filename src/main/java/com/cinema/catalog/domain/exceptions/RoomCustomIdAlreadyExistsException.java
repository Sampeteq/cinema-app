package com.cinema.catalog.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class RoomCustomIdAlreadyExistsException extends ValidationException {

    public RoomCustomIdAlreadyExistsException() {
        super("A room roomCustomId already exists");
    }
}
