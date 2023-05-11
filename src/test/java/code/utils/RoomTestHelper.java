package code.utils;

import code.rooms.domain.Room;

import java.util.UUID;

public class RoomTestHelper {

    public static Room createRoom() {
        return Room
                .builder()
                .id(1L)
                .customId("1")
                .rowsQuantity(10)
                .seatsInOneRowQuantity(15)
                .build();
    }
}
