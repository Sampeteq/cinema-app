package com.cinema.rooms.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class RoomsNoAvailableException extends ValidationException {

    public RoomsNoAvailableException() {
        super("No available rooms");
    }
}
