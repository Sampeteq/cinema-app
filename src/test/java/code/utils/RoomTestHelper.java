package code.utils;

import code.rooms.domain.Room;

import java.util.UUID;

public class RoomTestHelper {

    public static Room createSampleRoom() {
        return Room
                .builder()
                .id(UUID.randomUUID())
                .number(1)
                .rowsQuantity(10)
                .seatsInOneRowQuantity(15)
                .build();
    }
}
