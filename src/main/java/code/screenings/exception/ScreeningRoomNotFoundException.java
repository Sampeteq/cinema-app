package code.screenings.exception;

import java.util.UUID;

public class ScreeningRoomNotFoundException extends ScreeningException {

    public ScreeningRoomNotFoundException(UUID screeningRoomUUID) {
        super("Screening room not found: " + screeningRoomUUID);
    }
}
