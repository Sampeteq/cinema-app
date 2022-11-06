package code.bookings.exception;

import java.util.UUID;

public class BookingAlreadyCancelled extends BookingException {

    public BookingAlreadyCancelled(UUID ticketUUID) {
        super("Reservation already cancelled: " + ticketUUID);
    }
}
