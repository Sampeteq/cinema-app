package code.reservations.exception;

public class TooLateToReservationException extends ReservationException {

    public TooLateToReservationException() {
        super("Too late to reservation.");
    }
}
