package com.cinema.users.exceptions;

public class UserMailNotUniqueException extends RuntimeException {

    public UserMailNotUniqueException() {
        super("User mail is not unique");
    }
}
