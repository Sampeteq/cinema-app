package code.bookings.infrastructure.exceptions;

import code.bookings.domain.exceptions.BookingException;

public class BookingNotFoundException extends BookingException {

    public BookingNotFoundException() {
        super("Booking not found");
    }
}
