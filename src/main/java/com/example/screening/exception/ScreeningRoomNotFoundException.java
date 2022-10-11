package com.example.screening.exception;

import java.util.UUID;

public class ScreeningRoomNotFoundException extends ScreeningException {

    public ScreeningRoomNotFoundException(UUID roomUuid) {
        super("A screening room not found: " + roomUuid);
    }
}
