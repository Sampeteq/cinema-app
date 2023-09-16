package com.cinema.bookings.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class BookingAlreadyCancelledException extends ValidationException {

    public BookingAlreadyCancelledException() {
        super("Booking already cancelled");
    }
}
