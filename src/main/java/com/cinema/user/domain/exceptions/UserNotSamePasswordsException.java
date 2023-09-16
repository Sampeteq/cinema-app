package com.cinema.user.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class UserNotSamePasswordsException extends ValidationException {

    public UserNotSamePasswordsException() {
        super("Not same passwords");
    }
}
