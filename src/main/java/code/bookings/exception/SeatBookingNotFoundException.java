package code.bookings.exception;

public class SeatBookingNotFoundException extends BookingException {

    public SeatBookingNotFoundException() {
        super("Booking not found");
    }
}
