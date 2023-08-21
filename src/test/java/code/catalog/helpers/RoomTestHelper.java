package code.catalog.helpers;

import code.catalog.domain.Room;

public final class RoomTestHelper {

    private RoomTestHelper() {
    }

    public static Room createRoom() {
        var customId = "1";
        var rowsNumber = 15;
        var rowSeatsNumber = 10;
        return Room.create(
                customId,
                rowsNumber,
                rowSeatsNumber
        );
    }
}
