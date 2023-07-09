package code.catalog.helpers;

import code.catalog.domain.Room;

public class RoomTestHelper {

    public static Room createRoom() {
        return Room.builder()
                .customId("1")
                .rowsQuantity(10)
                .seatsInOneRowQuantity(15).build();
    }
}
