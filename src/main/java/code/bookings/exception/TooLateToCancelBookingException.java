package code.bookings.exception;

import java.util.UUID;

public class TooLateToCancelBookingException extends BookingException {

    public TooLateToCancelBookingException(UUID ticketUUID) {
        super("Too late to cancel reservation: " + ticketUUID);
    }
}
