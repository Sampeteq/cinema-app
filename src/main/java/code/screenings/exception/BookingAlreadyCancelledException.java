package code.screenings.exception;

import java.util.UUID;

public class BookingAlreadyCancelledException extends BookingException {

    public BookingAlreadyCancelledException(UUID seatId) {
        super("Seat booking already cancelled: " + seatId);
    }
}
