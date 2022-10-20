package code.screenings.exception;

import java.util.UUID;

public class ScreeningRoomBusyException extends ScreeningException {

    public ScreeningRoomBusyException(UUID roomUUID) {
        super("Screening room is busy: " + roomUUID);
    }
}
