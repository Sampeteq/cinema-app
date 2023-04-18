package code.rooms.client.dto;

import java.util.UUID;

public record RoomDto(
        UUID id,
        int number,
        int rowsQuantity,
        int seatsInOneRowQuantity,
        int seatsQuantity
) {
}
