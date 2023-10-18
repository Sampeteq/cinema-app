package com.cinema.users.domain.exceptions;

public class UserMailAlreadyExistsException extends RuntimeException {

    public UserMailAlreadyExistsException() {
        super("User with this email already exists");
    }
}
