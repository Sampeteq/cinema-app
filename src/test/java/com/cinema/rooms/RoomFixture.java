package com.cinema.rooms;

import com.cinema.rooms.domain.Room;

public class RoomFixture {

    private static final String ID = "1";
    private static final int ROWS_NUMBER = 10;
    private static final int ROW_SEATS_NUMBER = 15;

    public static Room createRoom() {
        return new Room(
                ID,
                ROWS_NUMBER,
                ROW_SEATS_NUMBER
        );
    }
}
