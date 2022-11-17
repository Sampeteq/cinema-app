package code.screenings.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ScreeningRoomDTO(
        UUID id,
        int number,
        int rowsQuantity,
        int seatsInOneRowQuantity,
        int seatsQuantity
) {
}
