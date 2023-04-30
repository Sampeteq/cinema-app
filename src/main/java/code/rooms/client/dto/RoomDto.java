package code.rooms.client.dto;

import java.util.UUID;

public record RoomDto(
        UUID id,
        String customId,
        int rowsQuantity,
        int seatsInOneRowQuantity,
        int seatsQuantity
) {
}
