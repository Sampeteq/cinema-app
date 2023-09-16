package com.cinema.tickets.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class TicketCancelTooLateException extends ValidationException {

    public TicketCancelTooLateException() {
        super("Too late to cancel ticket");
    }
}
