package code.screenings.exception;

import java.util.UUID;

public class TooLateToCancelBookingException extends BookingException {

    public TooLateToCancelBookingException(UUID ticketId) {
        super("Too late to cancel booking: " + ticketId);
    }
}
