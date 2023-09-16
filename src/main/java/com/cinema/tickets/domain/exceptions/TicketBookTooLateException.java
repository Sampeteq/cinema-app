package com.cinema.tickets.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class TicketBookTooLateException extends ValidationException {

    public TicketBookTooLateException() {
        super("Too late for ticket booking");
    }
}
