package code.reservations.exception;

import java.util.UUID;

public class TooLateToCancelReservationException extends ReservationException {

    public TooLateToCancelReservationException(UUID ticketUUID) {
        super("Too late to cancel reservation: " + ticketUUID);
    }
}
