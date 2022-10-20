package code.reservation.exception;

import java.util.UUID;

public class ReservationAlreadyCancelled extends ReservationException {

    public ReservationAlreadyCancelled(UUID ticketUUID) {
        super("Reservation already cancelled: " + ticketUUID);
    }
}
