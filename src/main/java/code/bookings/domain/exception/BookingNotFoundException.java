package code.bookings.domain.exception;

public class BookingNotFoundException extends BookingException {

    public BookingNotFoundException() {
        super("Booking not found");
    }
}
