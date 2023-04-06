package code.rooms.infrastructure.rest;

import java.util.UUID;

public record RoomDto(
        UUID id,
        int number,
        int rowsQuantity,
        int seatsInOneRowQuantity,
        int seatsQuantity
) {
}
