package code.rooms.dto;

import lombok.With;

import javax.validation.constraints.Positive;

@With
public record CreateRoomDto(
        @Positive
        int number,
        @Positive
        int rowsQuantity,
        @Positive
        int seatsQuantityInOneRow
) {
}
