package code.screenings.dto;

import java.util.UUID;

public record ScreeningRoomView(
        UUID id,
        int number,
        int rowsQuantity,
        int seatsInOneRowQuantity,
        int seatsQuantity
) {
}
