package code.screenings.exception;

import java.util.UUID;

public class BookingAlreadyCancelledException extends BookingException {

    public BookingAlreadyCancelledException(UUID ticketId) {
        super("Booking already cancelled: " + ticketId);
    }
}
