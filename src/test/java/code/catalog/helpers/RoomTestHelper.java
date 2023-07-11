package code.catalog.helpers;

import code.catalog.domain.Room;

public class RoomTestHelper {

    public static Room createRoom() {
        var customId = "1";
        var rowsQuantity = 15;
        var seatsQuantityInOneRow = 10;
        return Room.create(
                customId,
                rowsQuantity,
                seatsQuantityInOneRow
        );
    }
}
