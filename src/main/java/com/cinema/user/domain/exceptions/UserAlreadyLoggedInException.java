package com.cinema.user.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class UserAlreadyLoggedInException extends ValidationException {

    public UserAlreadyLoggedInException() {
        super("User already logged in");
    }
}
