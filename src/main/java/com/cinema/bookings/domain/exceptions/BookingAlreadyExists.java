package com.cinema.bookings.domain.exceptions;

import com.cinema.shared.exceptions.ValidationException;

public class BookingAlreadyExists extends ValidationException {

    public BookingAlreadyExists() {
        super("Booking already exists");
    }
}
