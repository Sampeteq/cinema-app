package com.cinema.catalog;

import com.cinema.rooms.domain.Room;

public final class RoomTestHelper {

    private static final String CUSTOM_ID = "1";

    private static final int ROWS_NUMBER = 15;

    private static final int ROW_SEATS_NUMBER = 10;

    private RoomTestHelper() {
    }

    public static Room createRoom() {
        return new Room(
                CUSTOM_ID,
                ROWS_NUMBER,
                ROW_SEATS_NUMBER
        );
    }
}
