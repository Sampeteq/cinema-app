package com.cinema.users.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class UserMailAlreadyExistsException extends ValidationException {

    public UserMailAlreadyExistsException() {
        super("User with this email already exists");
    }
}
