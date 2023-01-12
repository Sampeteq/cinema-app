package code.screenings.exception;

import java.util.UUID;

public class SeatBookingNotFoundException extends BookingException {

    public SeatBookingNotFoundException(UUID bookingId) {
        super("Booking not found: " + bookingId);
    }
}
