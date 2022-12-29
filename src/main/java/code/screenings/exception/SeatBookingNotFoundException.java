package code.screenings.exception;

import java.util.UUID;

public class SeatBookingNotFoundException extends BookingException {

    public SeatBookingNotFoundException(UUID ticketId) {
        super("Ticket not found: " + ticketId);
    }
}
