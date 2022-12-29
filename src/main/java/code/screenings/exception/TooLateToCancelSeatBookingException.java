package code.screenings.exception;

import java.util.UUID;

public class TooLateToCancelSeatBookingException extends BookingException {

    public TooLateToCancelSeatBookingException(UUID ticketId) {
        super("Too late to cancel booking: " + ticketId);
    }
}
