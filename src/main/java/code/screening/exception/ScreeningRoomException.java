package code.screening.exception;

import java.util.UUID;

public class ScreeningRoomException extends ScreeningException {

    private ScreeningRoomException(String message, ScreeningExceptionType type) {
        super(message, type);
    }

    public static ScreeningRoomException notFound(UUID roomUuid) {
        return new ScreeningRoomException(
                "Screening room not found: " + roomUuid,
                ScreeningExceptionType.NOT_FOUND
        );
    }

    public static ScreeningRoomException alreadyExists(int number) {
        return new ScreeningRoomException(
                "Screening room with such number already exists: " + number,
                ScreeningExceptionType.NOT_UNIQUE
        );
    }
}
