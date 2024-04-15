package com.cinema.users.application.exceptions;

public class UserMailNotUniqueException extends RuntimeException {

    public UserMailNotUniqueException() {
        super("User mail is not unique");
    }
}
