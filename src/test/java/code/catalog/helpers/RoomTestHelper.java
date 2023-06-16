package code.catalog.helpers;

import code.catalog.domain.Room;

public class RoomTestHelper {

    public static Room createRoom() {
        return Room.create(
                "1",
                10,
                15
        );
    }
}
