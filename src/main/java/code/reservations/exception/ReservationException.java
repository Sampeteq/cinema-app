package code.reservations.exception;

public abstract class ReservationException extends RuntimeException {

    public ReservationException(String message) {
        super(message);
    }
}
