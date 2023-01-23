package code.screenings.dto;

import java.util.UUID;

public record RoomDto(
        UUID id,
        int number,
        int rowsQuantity,
        int seatsInOneRowQuantity,
        int seatsQuantity
) {
}
