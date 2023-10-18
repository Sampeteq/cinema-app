package com.cinema.users.domain.exceptions;

public class UserNotSamePasswordsException extends RuntimeException {

    public UserNotSamePasswordsException() {
        super("Not same passwords");
    }
}
