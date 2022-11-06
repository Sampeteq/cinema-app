package code.bookings.exception;

import java.util.UUID;

public class BookingAlreadyCancelledException extends BookingException {

    public BookingAlreadyCancelledException(UUID ticketUUID) {
        super("Reservation already cancelled: " + ticketUUID);
    }
}
