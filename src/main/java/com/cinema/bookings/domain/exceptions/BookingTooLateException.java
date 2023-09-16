package com.cinema.bookings.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class BookingTooLateException extends ValidationException {

    public BookingTooLateException() {
        super("Too late for booking");
    }
}
