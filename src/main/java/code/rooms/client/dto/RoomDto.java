package code.rooms.client.dto;

import java.util.UUID;

public record RoomDto(
        Long id,
        String customId,
        int rowsQuantity,
        int seatsInOneRowQuantity,
        int seatsQuantity
) {
}
