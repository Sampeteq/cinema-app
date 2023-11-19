package com.cinema.halls.domain.exceptions;

public class HallIdAlreadyExistsException extends RuntimeException {

    public HallIdAlreadyExistsException() {
        super("A hall id already exists");
    }
}
