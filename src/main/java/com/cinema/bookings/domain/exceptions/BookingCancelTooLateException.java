package com.cinema.bookings.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class BookingCancelTooLateException extends ValidationException {

    public BookingCancelTooLateException() {
        super("Too late to cancel booking exception");
    }
}
